package com.reporting.mocks.process;

import java.util.Collection;
import java.util.UUID;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.generators.TradeGenerator;
import com.reporting.mocks.generators.process.streaming.StreamRiskResultPublisherThread;
import com.reporting.mocks.interfaces.persistence.ICalculationContextStore;
import com.reporting.mocks.interfaces.persistence.IMarketStore;
import com.reporting.mocks.interfaces.persistence.IRiskResultStore;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.process.intraday.IntradayMarketEventRiskProducerThread;
import com.reporting.mocks.process.intraday.IntradayTradeEventProducerThread;
import com.reporting.mocks.process.intraday.IntradayTradeEventRiskProducerThread;
import com.reporting.mocks.process.risks.RiskRunType;
import com.reporting.mocks.process.risks.TradePopulationRiskProducerThread;
import com.reporting.mocks.process.risks.TradePopulationRiskRunRequest;

public class ProcessSimulator {
    protected UUID id;
    protected ApplicationConfig appConfig;
    protected PricingGroupConfig config;
    protected ITradeStore tradeStore;
    protected ICalculationContextStore calculationContextStore;
    protected IMarketStore marketStore;
    protected TradeGenerator tradeGenerator;
    protected IRiskResultStore riskResultStore;

    IResultPublisher resultPublisher;

    ProcessEventQueues processEventQueues;

    protected ThreadGroup threadGroup;

    public ProcessSimulator(PricingGroupConfig config,
                            ApplicationConfig appConfig,
                            ICalculationContextStore calculationContextStore,
                            IMarketStore marketStore,
                            ITradeStore tradeStore,
                            IRiskResultStore riskResultStore,
                            IResultPublisher resultPublisher) {
        this.id = UUID.randomUUID();
        this.appConfig = appConfig;
        this.config = config;
        this.tradeStore = tradeStore;
        this.calculationContextStore = calculationContextStore;
        this.marketStore = marketStore;
        this.riskResultStore = riskResultStore;
        this.resultPublisher = resultPublisher;
        this.tradeGenerator = new TradeGenerator(config.getTradeConfig());
        this.processEventQueues = new JavaProcessEventQueues();
    }

    public Collection<TradePopulation> getTradePopulations() {
        return this.tradeStore.getAllTradePopulation();
    }

    public TradePopulation getTradePopulation(TradePopulationId tradePopulationId) {
        return this.tradeStore.getTradePopulationById(tradePopulationId);
    }

    public PricingGroup getPricingGroupId() {
        return this.config.getPricingGroupId();
    }

    protected void init() {
        if (this.threadGroup == null || this.threadGroup.isDestroyed()) {
            this.threadGroup = new ThreadGroup("PricingGroup: " + config.getPricingGroupId());

            // initiate construction of initial trade population
            for (int i = 0; i < config.getTradeConfig().getStartingTradeCount(); i++) {
                Trade newTrade = this.tradeGenerator.generateOneOtc();
                this.tradeStore.add(newTrade);
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
            new Thread(threadGroup,
                    new TradePopulationRiskProducerThread(
                            this.processEventQueues.getRiskRunRequestQueue(),
                            this.processEventQueues.getRiskStreamMessageQueue(),
                            this.config,
                            this.calculationContextStore,
                            this.tradeStore,
                            this.resultPublisher
                    ),
                    "TradePopulationRiskProducerThread").start();

           new Thread(threadGroup,
                   new StreamRiskResultPublisherThread(
                    this.processEventQueues.getRiskStreamMessageQueue(),            // input queue to process
                    this.config,
                    this.calculationContextStore,
                    this.resultPublisher,
                    this.riskResultStore
            ), "StreamRiskResultPublisherThread").start();;

            // kick-off start-of-day

            // create a new trade population for the start of day
            TradePopulation sodTradePopulation = this.tradeStore.create(DataMarkerType.SOD);
            // send a trade population risk request to have all risks calculated for the start of day trades
            this.processEventQueues.getRiskRunRequestQueue().add(
                    new TradePopulationRiskRunRequest(
                            RiskRunType.StartOfDay,
                            cc.getCalculationContextId(),
                            this.config.findAllRiskTypes(),
                            sodTradePopulation.getId()
                    )
            );

            // kick-off end-of-day

//                EndofDayRiskEventProducerThread eodThread = new EndofDayRiskEventProducerThread(
//                        this.config.getPricingGroupId(),
//                        this.config.getEndofdayConfig(),
//                        this.tradeStore,
//                        this.marketStore,
//                        this.calculationContextStore,
//                        this.processEventQueues.getRiskRunRequestQueue(),               // output queue from process
//                        this.resultPublisher);
//                new Thread(threadGroup, eodThread, "EndofDayRiskEvent").start();


            new Thread(threadGroup,
                    new IntradayMarketEventRiskProducerThread(
                            this.config,
                            this.tradeStore,
                            this.marketStore,
                            this.calculationContextStore,
                            this.processEventQueues.getRiskRunRequestQueue(),
                            this.resultPublisher,
                            sodMarket
                    ),
                    "IntradayMarketEventRiskProducerThread").start();

            new Thread(threadGroup,
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
                    "IntradayTradeEventRiskProducerThread").start();


            new Thread(threadGroup,
                    new IntradayTradeEventProducerThread(
                            this.config.getTradeConfig(),
                            this.tradeStore,
                            this.tradeGenerator,
                            this.processEventQueues.getTradeLifecycleQueue(),
                            this.resultPublisher
                    ),
                    "IntradayTradeEventProducerThread").start();
        }
    }

    public void stop() {
        if (this.threadGroup != null && !this.threadGroup.isDestroyed()) {
            this.threadGroup.interrupt();;
        }
    }

    public PricingGroupConfig start() {
        return this.start(this.config);
    }

    public PricingGroupConfig start(PricingGroupConfig config) {
        if (this.threadGroup == null || this.threadGroup.isDestroyed()) {
            if (this.config.getPricingGroupId() == config.getPricingGroupId()) {
                this.config = config;
                this.init();
                return config;
            }
        }
        return null;
    }
}
