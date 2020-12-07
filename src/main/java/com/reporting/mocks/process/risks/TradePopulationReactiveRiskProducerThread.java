package com.reporting.mocks.process.risks;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.generators.IRiskGeneratorLite;
import com.reporting.mocks.generators.RiskGeneratorFactory;
import com.reporting.mocks.generators.process.streaming.RiskStreamMessage;
import com.reporting.mocks.interfaces.persistence.ICalculationContextStore;
import com.reporting.mocks.interfaces.persistence.ITradePopulation;
import com.reporting.mocks.interfaces.persistence.ITradePopulationReactive;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.TradeType;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradePopulationReactiveRiskProducerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(TradePopulationReactiveRiskProducerThread.class.getName());
    protected BlockingQueue<TradePopulationReactiveRiskRunRequest> tradePopulationRiskRunRequests;
    protected BlockingQueue<RiskStreamMessage<? extends Risk>> riskResultPubisherQueue;
    protected ICalculationContextStore calculationContextStore;
    protected ITradeStore tradeStore;
    protected IResultPublisher resultPublisher;
    protected PricingGroupConfig pricingGroupConfig;


    public TradePopulationReactiveRiskProducerThread(BlockingQueue<TradePopulationReactiveRiskRunRequest> riskRunRequestQueue,
                                             BlockingQueue<RiskStreamMessage<? extends Risk>> riskResultPubisherQueue,
                                             PricingGroupConfig pricingGroupConfig,
                                             ICalculationContextStore calculationContextStore,
                                             ITradeStore tradeStore,
                                             IResultPublisher resultPublisher) {
        this.tradePopulationRiskRunRequests = riskRunRequestQueue;
        this.riskResultPubisherQueue = riskResultPubisherQueue;
        this.calculationContextStore = calculationContextStore;
        this.tradeStore = tradeStore;
        this.resultPublisher = resultPublisher;
        this.pricingGroupConfig = pricingGroupConfig;
    }


    @Override
    public void run() {
        try {
            while (true) {
                TradePopulationReactiveRiskRunRequest tradePopulationRiskRunRequest = this.tradePopulationRiskRunRequests.take();

                List<RiskType> risksToRun = tradePopulationRiskRunRequest.getRisksToRun();

                ITradePopulationReactive tradePopulationReactive = this.tradeStore.getTradePopulationReactiveById(tradePopulationRiskRunRequest.getTradePopulationId());

                /*
                    Calculate the number of risks to be sent
                 */
                int riskCount = 0;
                for(TradeType tradeType : tradePopulationReactive.getTradeTypes()) {
                    List<RiskType> tradeRisks = this.pricingGroupConfig.getTradeConfig().findRiskByTradeType(tradeType);
                    int tradeCount = tradePopulationReactive.getTradeCountByTradeType(tradeType);
                    for(RiskType riskType : tradeRisks) {
                        if (risksToRun.contains(riskType)) {
                            riskCount += tradeCount;
                        }
                    }
                }

                RiskRunId riskRunId = new RiskRunId(this.pricingGroupConfig.getPricingGroupId().getName());
                AtomicInteger riskNo = new AtomicInteger();
//
//                tradePopulationReactive.getTrades().subscribe(
//                        trade -> {
//                            List<RiskType> tradeRisks = this.pricingGroupConfig.getTradeConfig().findRiskByTradeType(trade.getTradeType());
//                            for(RiskType riskType : tradeRisks) {
//                                if (risksToRun.contains(riskType)) {
//                                    IRiskGeneratorLite<? extends Risk> riskGenerator = RiskGeneratorFactory.getGeneratorLite(riskType);
//                                    MarketEnvId marketEnvId = calculationContext.get(riskType);
//                                    riskNo.getAndIncrement();
//                                    Risk risk = riskGenerator.generate(marketEnvId, trade);
//                                    RiskStreamMessage<? extends Risk> riskStreamMsg = new RiskStreamMessage<>(
//                                            calculationContext.getCalculationContextId(),
//                                            riskRunId,
//                                            RiskRunType.Intraday,
//                                            finalRiskCount1,
//                                            riskNo.get(),
//                                            risk,
//                                            false);
//
//                                    this.riskResultPubisherQueue.add(riskStreamMsg);
//                                    System.out.println("Published " + risk);
//                                }
//                            }
//                        }
//                );


                MarketEnvId marketEnvId = tradePopulationRiskRunRequest.marketEnvId;
                for(TradeType tradeType : tradePopulationReactive.getTradeTypes()) {
                    List<RiskType> tradeRisks = this.pricingGroupConfig.getTradeConfig().findRiskByTradeType(tradeType);
                    for(RiskType riskType : tradeRisks) {
                        if (risksToRun.contains(riskType)) {
                            IRiskGeneratorLite<? extends Risk> riskGenerator = RiskGeneratorFactory.getGeneratorLite(riskType);
                            int finalRiskCount = riskCount;
                            tradePopulationReactive.getTradesByType(tradeType).subscribe(
                                    trade -> {
                                                riskNo.getAndIncrement();
                                                Risk risk = riskGenerator.generate(marketEnvId, trade);
                                                RiskStreamMessage<? extends Risk> riskStreamMsg = new RiskStreamMessage<>(
                                                        tradePopulationRiskRunRequest.getCalculationContextId(),
                                                        riskRunId,
                                                        RiskRunType.Intraday,
                                                        finalRiskCount,
                                                        riskNo.get(),
                                                        risk,
                                                        false);

                                                this.riskResultPubisherQueue.add(riskStreamMsg);
                                    },
                                    err -> System.out.println("Error :: " + err),  //consumer to handle the error
                                    () -> System.out.println("Successfully completed")
                            );

                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            // LOGGER.log( Level.FINE, "processing {0} entries in loop", list.size() );
            LOGGER.log(Level.FINE, "thread interrupted");
        }
    }
}
