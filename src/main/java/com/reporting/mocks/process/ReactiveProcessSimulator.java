package com.reporting.mocks.process;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.generators.process.streaming.StreamRiskResultPublisherThread;
import com.reporting.mocks.interfaces.persistence.*;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.process.intraday.IntradayMarketEventReactiveRiskProducerThread;
import com.reporting.mocks.process.intraday.IntradayTradeEventProducerThread;
import com.reporting.mocks.process.intraday.IntradayTradeEventRiskProducerThread;
import com.reporting.mocks.process.risks.*;

public class ReactiveProcessSimulator extends ProcessSimulator {
    public ReactiveProcessSimulator(PricingGroupConfig config, ApplicationConfig appConfig, ICalculationContextStore calculationContextStore, IMarketStore marketStore, ITradeStore tradeStore, IRiskResultStore riskResultStore, IResultPublisher resultPublisher) {
        super(config, appConfig, calculationContextStore, marketStore, tradeStore, riskResultStore, resultPublisher);
    }

    @Override
    protected void init() {
        if (this.threadGroup == null || this.threadGroup.isDestroyed()) {
            this.threadGroup = new ThreadGroup("PricingGroup: " + config.getPricingGroupId());

            ITradePopulationLive liveTrades = this.tradeStore.getTradePopulationLive();

            // initiate construction of initial trade population
            for (int i = 0; i < config.getTradeConfig().getStartingTradeCount(); i++) {
                Trade newTrade = this.tradeGenerator.generateOneOtc();
                liveTrades.add(newTrade);
                this.resultPublisher.publishIntradayTrade(new TradeLifecycle(
                        TradeLifecycleType.New,
                        null,
                        newTrade
                ));
            }

            // initialize the start calculation context
            CalculationContext cc = this.calculationContextStore.getCurrentContext();
            MarketEnv sodMarket = marketStore.create(DataMarkerType.SOD);
            if (cc == null) {
                cc = this.calculationContextStore.create();
                cc.update(this.config.findAllRiskTypes(), sodMarket);
                this.calculationContextStore.setCurrentContext(cc);
            }

            this.resultPublisher.publish(cc);

            // Create the thread that receives requests to calculate risks for trade populations
            Thread tprpt = new Thread(threadGroup,
                    new TradePopulationReactiveRiskProducerThread(
                            this.processEventQueues.getReactiveRiskRunRequestQueue(),
                            this.processEventQueues.getRiskStreamMessageQueue(),
                            this.config,
                            this.calculationContextStore,
                            this.tradeStore,
                            this.resultPublisher
                    ),
                    "TradePopulationRiskProducerThread");
            tprpt.setPriority(10);
            tprpt.start();

            Thread srrpt = new Thread(threadGroup,
                    new StreamRiskResultPublisherThread(
                            this.processEventQueues.getRiskStreamMessageQueue(),            // input queue to process
                            this.config,
                            this.calculationContextStore,
                            this.resultPublisher,
                            this.riskResultStore
                    ), "StreamRiskResultPublisherThread");
            srrpt.setPriority(10);
            srrpt.start();

            // kick-off start-of-day

            // create a new trade population for the start of day
            ITradePopulationReactive sodTradePopulation = this.tradeStore.createReactiveSnapShot(DataMarkerType.SOD);

            // send a trade population risk request to have all risks calculated for the start of day trades
            this.processEventQueues.getReactiveRiskRunRequestQueue().add(
                    new TradePopulationReactiveRiskRunRequest(
                            RiskRunType.StartOfDay,
                            cc.getCalculationContextId(),
                            sodMarket.getId(),
                            this.config.findAllRiskTypes(),
                            sodTradePopulation.getId()
                    )
            );

            new Thread(threadGroup,
                    new IntradayMarketEventReactiveRiskProducerThread(
                            this.config,
                            this.tradeStore,
                            this.marketStore,
                            this.calculationContextStore,
                            this.processEventQueues.getReactiveRiskRunRequestQueue(),
                            this.resultPublisher,
                            sodMarket
                    ),
                    "IntradayMarketEventRiskProducerThread").start();

            Thread iterpt = new Thread(threadGroup,
                    new IntradayTradeEventRiskProducerThread(
                            this.config,
                            this.tradeStore,
                            this.marketStore,
                            this.calculationContextStore,
                            this.processEventQueues.getTradeLifecycleQueue(),
                            this.processEventQueues.getRiskStreamMessageQueue(),
                            this.resultPublisher,
                            sodMarket
                    ),
                    "IntradayTradeEventRiskProducerThread");
            iterpt.setPriority(1);
            iterpt.start();


            Thread itept = new Thread(threadGroup,
                    new IntradayTradeEventProducerThread(
                            this.config.getTradeConfig(),
                            this.tradeStore,
                            this.tradeGenerator,
                            this.processEventQueues.getTradeLifecycleQueue(),
                            this.resultPublisher
                    ),
                    "IntradayTradeEventProducerThread");
            itept.setPriority(1);
            itept.start();
        }
    }
}
