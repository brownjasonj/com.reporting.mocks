package com.reporting.mocks.process.intraday;

import com.reporting.mocks.configuration.IntradayConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.generators.RiskRunGenerator;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.persistence.CalculationContextStore;
import com.reporting.mocks.persistence.TradeStore;
import com.reporting.mocks.process.risks.requests.MTSRRiskRunRequest;
import com.reporting.mocks.process.risks.requests.STSRRiskRunRequest;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import com.reporting.mocks.process.risks.RiskRunType;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class IntradayRiskEventProducerThread implements Runnable {
    protected BlockingQueue<IntradayEvent<?>> intradayEventQueue;
    protected RiskRunPublisher riskPublisher;
    protected TradeStore tradeStore;
    protected IntradayConfig config;
    protected TradePopulation tradePopulation;
    protected IntradayCalculationSchedule calculationSchedule;
    protected CalculationContext currentCalculationContext;
    protected CalculationContextStore calculationContextStore;

    public IntradayRiskEventProducerThread(PricingGroup pricingGroup,
                                           IntradayConfig config,
                                           TradeStore tradeStore,
                                           CalculationContextStore calculationContextStore,
                                           BlockingQueue<IntradayEvent<?>> intradayEventQueue,
                                           RiskRunPublisher riskPublisher,
                                           MarketEnv market) {
        this.config = config;
        this.tradeStore = tradeStore;
        this.intradayEventQueue = intradayEventQueue;
        this.riskPublisher = riskPublisher;
        this.calculationSchedule = new IntradayCalculationSchedule();
        this.calculationContextStore = calculationContextStore;
        this.currentCalculationContext = calculationContextStore.add(new CalculationContext(pricingGroup));
        for(IntradayRiskType riskType : config.getRisks()) {
            this.calculationSchedule.add(riskType.getPeriodicity(), riskType.getRiskType());
            this.currentCalculationContext.add(riskType.getRiskType(), market);
        }
    }

    @Override
    public void run() {
        IntradayEvent<?> intradayEvent;
        try {
            while(true) {
                intradayEvent = intradayEventQueue.take();

                switch (intradayEvent.getType()) {
                    case Market: {
                        IntradayEvent<MarketEnv> marketEvent = (IntradayEvent<MarketEnv>)intradayEvent;

                        System.out.println("New Market " + marketEvent.getEvent().getId() + " asOf: " + marketEvent.getEvent().getAsOf().toString());
                        // run all appropriate risks for the new trade population
                        // 1. get a new trade population snapshot
                        // 2. run each risk on the trade population
                        // 3. chunk the risks into fragments
                        // 4. send to risk queue

                        // this gets and creates a new trade population
                        this.tradePopulation = this.tradeStore.getTradePopulation(DataMarkerType.IND);

                        // increment the risk schedule since a new market arrived
                        this.calculationSchedule.increment();

                        List<RiskType> risksToRun = this.calculationSchedule.getRisksToRun();

                        // set the markets for each of the risks to run with given market
                        this.currentCalculationContext = this.calculationContextStore.add(new CalculationContext(this.currentCalculationContext));
                        this.currentCalculationContext.update(risksToRun, marketEvent.getEvent());

                        // RiskRunRequest(RiskRunType type, MarketEnv marketEnv, TradePopulation tradePop, List< RiskType > riskTypes, int fragmentSize)
                        for(RiskType riskType : risksToRun) {
                            MTSRRiskRunRequest riskRunRequest = new MTSRRiskRunRequest(RiskRunType.Intraday, marketEvent.getEvent(), tradePopulation, riskType, 20);
                            List<RiskRunResult> results = RiskRunGenerator.generate(this.tradePopulation, riskRunRequest);
                            for(RiskRunResult r : results) {
                                riskPublisher.publish(r);
                            }
                        }
                    }
                    break;
                    case Trade: {
                        IntradayEvent<TradeLifecycle> tradeEvent = (IntradayEvent<TradeLifecycle>)intradayEvent;
                        System.out.println("Trade Event " + tradeEvent.getEvent().getLifecycleType() + " trade: " + tradeEvent.getEvent().getTrade().toString());
                        // 1. calculate all risks for the current trade (if new)
                        TradeLifecycle tradeLifecycleEvent = tradeEvent.getEvent();
                        switch (tradeLifecycleEvent.getLifecycleType()) {
                            case New: {
                                // before calculating risk, check that the trade was not in the current TradePopulation
                                // if it is, then the risk was already calculated for it, so skip this trade
                                Trade trade = tradeLifecycleEvent.getTrade();
                                if (trade != null) {
                                    Trade existingTrade = this.tradePopulation.getTrade(trade.getTcn());
                                    if (existingTrade == null)  {
                                        // caclulate all the risks for this trade, since it is not in current population or has a different version
                                        for(IntradayRiskType irt : config.getRisks()) {
                                            STSRRiskRunRequest riskRunRequest = new STSRRiskRunRequest(
                                                    RiskRunType.Intraday,
                                                    this.currentCalculationContext.get(irt.getRiskType()),
                                                    trade,
                                                    irt.getRiskType());
                                            List<RiskRunResult> results = RiskRunGenerator.generate(riskRunRequest);
                                            for(RiskRunResult r : results) {
                                                riskPublisher.publish(r);
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                            case Modify: {
                                Trade trade = tradeLifecycleEvent.getTrade();
                                if (trade != null) {
                                    Trade existingTrade = this.tradePopulation.getTrade(trade.getTcn());
                                    if (existingTrade == null || trade.getVersion() != existingTrade.getVersion())  {
                                        // caclulate all the risks for this trade, since it is not in current population or has a different version
                                        for(IntradayRiskType irt : config.getRisks()) {
                                            STSRRiskRunRequest riskRunRequest = new STSRRiskRunRequest(
                                                    RiskRunType.Intraday,
                                                    this.currentCalculationContext.get(irt.getRiskType()),
                                                    trade,
                                                    irt.getRiskType());
                                            List<RiskRunResult> results = RiskRunGenerator.generate(riskRunRequest);
                                            for(RiskRunResult r : results) {
                                                riskPublisher.publish(r);
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                            case Delete: {
                                // send something????
                            }
                            break;
                            default:
                                break;
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
