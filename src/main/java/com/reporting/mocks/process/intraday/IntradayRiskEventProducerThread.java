package com.reporting.mocks.process.intraday;

import com.reporting.mocks.configuration.IntradayConfig;
import com.reporting.mocks.endpoints.IResultPublisher;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.persistence.*;
import com.reporting.mocks.process.risks.RiskRunRequest;
import com.reporting.mocks.process.risks.RiskRunType;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class IntradayRiskEventProducerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( IntradayRiskEventProducerThread.class.getName() );
    protected BlockingQueue<IntradayEvent<?>> intradayEventQueue;
    protected BlockingQueue<RiskRunRequest> riskRunRequestQueue;
    protected IResultPublisher riskPublisher;
    protected ITradeStore tradeStore;
    protected IMarketStore marketStore;
    protected IntradayConfig config;
    protected TradePopulationMutable tradePopulation;
    protected IntradayCalculationSchedule calculationSchedule;
    protected CalculationContext currentCalculationContext;
    protected ICalculationContextStore calculationContextStore;


    public IntradayRiskEventProducerThread(PricingGroup pricingGroup,
                                           IntradayConfig config,
                                           ITradeStore tradeStore,
                                           IMarketStore IMarketStore,
                                           ICalculationContextStore ICalculationContextStore,
                                           BlockingQueue<IntradayEvent<?>> intradayEventQueue,
                                           BlockingQueue<RiskRunRequest> riskRunRequestQueue,
                                           IResultPublisher riskPublisher,
                                           MarketEnv market) {
        this.config = config;
        this.tradeStore = tradeStore;
        this.marketStore = IMarketStore;
        this.intradayEventQueue = intradayEventQueue;
        this.riskRunRequestQueue = riskRunRequestQueue;
        this.riskPublisher = riskPublisher;
        this.calculationSchedule = new IntradayCalculationSchedule();
        this.calculationContextStore = ICalculationContextStore;
        this.currentCalculationContext = ICalculationContextStore.create();
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

                try {
                    switch (intradayEvent.getType()) {
                        case Market: {
                            IntradayEvent<MarketEnv> marketEvent = (IntradayEvent<MarketEnv>) intradayEvent;

                            System.out.println("New Market " + marketEvent.getEvent().getId() + " asOf: " + marketEvent.getEvent().getAsOf().toString());
                            // run all appropriate risks for the new trade population
                            // 1. getTradeByTcn a new trade population snapshot
                            // 2. run each risk on the trade population
                            // 3. chunk the risks into fragments
                            // 4. send to risk queue


                            riskPublisher.publish(marketEvent.getEvent());

                            // this gets and creates a new trade population
                            this.tradePopulation = this.tradeStore.create(DataMarkerType.IND);

                            // increment the risk schedule since a new market arrived
                            this.calculationSchedule.increment();

                            List<RiskType> risksToRun = this.calculationSchedule.getRisksToRun();

                            // set the markets for each of the risks to run with given market
                            this.currentCalculationContext = this.calculationContextStore.getCurrentContext();
                            this.currentCalculationContext = this.calculationContextStore.createCopy(this.currentCalculationContext);
                            this.currentCalculationContext.update(risksToRun, marketEvent.getEvent());
                            this.calculationContextStore.setCurrentContext(this.currentCalculationContext);

                            this.riskPublisher.publish(this.currentCalculationContext);

                            this.riskRunRequestQueue.add(new RiskRunRequest(
                                    RiskRunType.Intraday,
                                    this.currentCalculationContext.getCalculationContextId(),
                                    null,
                                    this.tradePopulation.getId(),
                                    risksToRun,
                                    null,
                                    false // this is NOT a delete event
                            ));
                        }
                        break;
                        case Trade: {
                            IntradayEvent<TradeLifecycle> tradeEvent = (IntradayEvent<TradeLifecycle>) intradayEvent;
                            System.out.println("Trade Event " + tradeEvent.getEvent().getLifecycleType() + " trade: " + tradeEvent.getEvent().getTrade().toString());
                            // 1. calculate all risks for the current trade (if new)
                            TradeLifecycle tradeLifecycleEvent = tradeEvent.getEvent();
                            this.riskPublisher.publishIntradayTrade(tradeLifecycleEvent);
                            // grab the latest calculation context from the store as it may have changed due to an end of day event
                            this.currentCalculationContext = this.calculationContextStore.getCurrentContext();
                            switch (tradeLifecycleEvent.getLifecycleType()) {
                                case New: {
                                    // before calculating risk, check that the trade was not in the current TradePopulation
                                    // if it is, then the risk was already calculated for it, so skip this trade
                                    Trade trade = tradeLifecycleEvent.getTrade();
                                    if (trade != null) {
                                        Trade existingTrade = this.tradePopulation.getTrade(trade.getTcn());
                                        if (existingTrade == null) {
                                            // add the trade to the current tradepopulation
                                            this.tradePopulation.add(trade);
                                            // caclulate all the risks for this trade, since it is not in current population or has a different version
                                            this.riskRunRequestQueue.add(new RiskRunRequest(
                                                    RiskRunType.Intraday,
                                                    this.currentCalculationContext.getCalculationContextId(),
                                                    null,
                                                    this.tradePopulation.getId(),
                                                    config.getRisks().stream().map(itr -> itr.getRiskType()).collect(Collectors.toList()),
                                                    trade,
                                                    false // this is NOT a delete event
                                            ));
                                        }
                                    }
                                }
                                break;
                                case Modify: {
                                    Trade trade = tradeLifecycleEvent.getTrade();
                                    if (trade != null) {
                                        Trade existingTrade = this.tradePopulation.getTrade(trade.getTcn());
                                        if (existingTrade != null && trade.getVersion() != existingTrade.getVersion()) {
                                            this.currentCalculationContext = this.calculationContextStore.getCurrentContext();
                                            // modify trade in the current trade population
                                            this.tradePopulation.delete(existingTrade.getTcn());
                                            this.riskRunRequestQueue.add(new RiskRunRequest(
                                                    RiskRunType.Intraday,
                                                    this.currentCalculationContext.getCalculationContextId(),
                                                    null,
                                                    this.tradePopulation.getId(),
                                                    config.getRisks().stream().map(itr -> itr.getRiskType()).collect(Collectors.toList()),
                                                    existingTrade,
                                                    true  // this IS a delete event
                                            ));
                                            this.tradePopulation.add(trade);
                                            // caclulate all the risks for this trade, since it is not in current population or has a different version
                                            this.riskRunRequestQueue.add(new RiskRunRequest(
                                                    RiskRunType.Intraday,
                                                    this.currentCalculationContext.getCalculationContextId(),
                                                    null,
                                                    this.tradePopulation.getId(),
                                                    config.getRisks().stream().map(itr -> itr.getRiskType()).collect(Collectors.toList()),
                                                    trade,
                                                    false  // this is NOT a delete event
                                            ));
                                        }
                                    }
                                }
                                break;
                                case Delete: {
                                    // send something????
                                    Trade trade = tradeLifecycleEvent.getTrade();
                                    if (trade != null) {
                                        Trade existingTrade = this.tradePopulation.getTrade(trade.getTcn());
                                        if (existingTrade != null) {
                                            this.tradePopulation.delete(existingTrade.getTcn());
                                            this.riskRunRequestQueue.add(new RiskRunRequest(
                                                    RiskRunType.Intraday,
                                                    this.currentCalculationContext.getCalculationContextId(),
                                                    null,
                                                    this.tradePopulation.getId(),
                                                    config.getRisks().stream().map(itr -> itr.getRiskType()).collect(Collectors.toList()),
                                                    existingTrade,
                                                    true  // this IS a delete event
                                            ));
                                        }
                                    }
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
                catch (Exception e) {
                    LOGGER.log( Level.FINE, "an exception occurred");
                }
            }
        } catch (InterruptedException e) {
            // LOGGER.log( Level.FINE, "processing {0} entries in loop", list.size() );
            LOGGER.log( Level.FINE, "thread interrupted");
        }
    }
}
