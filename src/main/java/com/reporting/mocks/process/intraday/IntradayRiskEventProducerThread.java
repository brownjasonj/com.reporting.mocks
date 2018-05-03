package com.reporting.mocks.process.intraday;

import com.reporting.mocks.configuration.IntradayConfig;
import com.reporting.mocks.generators.RiskRunGenerator;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.persistence.TradeStore;
import com.reporting.mocks.process.risks.requests.MTSRRiskRunRequest;
import com.reporting.mocks.process.risks.requests.STSRRiskRunRequest;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import com.reporting.mocks.process.risks.RiskRunType;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class IntradayRiskEventProducerThread implements Runnable {
    protected BlockingQueue<IntradayEvent<?>> intradayEventQueue;
    protected BlockingQueue<RiskRunResult> riskResultQueue;
    protected TradeStore tradeStore;
    protected IntradayConfig config;
    protected MarketEnv market;
    protected TradePopulation tradePopulation;

    public IntradayRiskEventProducerThread(IntradayConfig config, TradeStore tradeStore, BlockingQueue<IntradayEvent<?>> intradayEventQueue, BlockingQueue<RiskRunResult> riskResultQueue) {
        this.config = config;
        this.tradeStore = tradeStore;
        this.intradayEventQueue = intradayEventQueue;
        this.riskResultQueue = riskResultQueue;
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
                        this.market = marketEvent.getEvent();

                        System.out.println("New Market " + marketEvent.getEvent().getId() + " asOf: " + marketEvent.getEvent().getAsOf().toString());
                        // run all appropriate risks for the new trade population
                        // 1. get a new trade population snapshot
                        // 2. run each risk on the trade population
                        // 3. chunk the risks into fragments
                        // 4. send to risk queue

                        // this gets and creates a new trade population
                        this.tradePopulation = this.tradeStore.getTradePopulation(DataMarkerType.IND);

                        // RiskRunRequest(RiskRunType type, MarketEnv marketEnv, TradePopulation tradePop, List< RiskType > riskTypes, int fragmentSize)
                        for(IntradayRiskType irt : config.getRisks()) {
                            if (irt.getPeriodicity() == 0) {
                                MTSRRiskRunRequest riskRunRequest = new MTSRRiskRunRequest(RiskRunType.Intraday, this.market, tradePopulation, irt.getRiskType(), 20);
                                List<RiskRunResult> results = RiskRunGenerator.generate(this.tradePopulation, riskRunRequest);
                                for(RiskRunResult r : results) {
                                    riskResultQueue.put(r);
                                }
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
                                            STSRRiskRunRequest riskRunRequest = new STSRRiskRunRequest(RiskRunType.Intraday, this.market, trade, irt.getRiskType());
                                            List<RiskRunResult> results = RiskRunGenerator.generate(riskRunRequest);
                                            for(RiskRunResult r : results) {
                                                riskResultQueue.put(r);
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
                                            STSRRiskRunRequest riskRunRequest = new STSRRiskRunRequest(RiskRunType.Intraday, this.market, trade, irt.getRiskType());
                                            List<RiskRunResult> results = RiskRunGenerator.generate(riskRunRequest);
                                            for(RiskRunResult r : results) {
                                                riskResultQueue.put(r);
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
