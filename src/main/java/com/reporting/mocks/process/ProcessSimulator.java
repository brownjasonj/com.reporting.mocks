package com.reporting.mocks.process;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.JavaQueue.RiskRunConsumerThread;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.endpoints.kafka.RiskRunResultKafkaPublisher;
import com.reporting.mocks.generators.RiskRunGeneratorThread;
import com.reporting.mocks.generators.TradeGenerator;
import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.persistence.*;
import com.reporting.mocks.process.endofday.EndofDayRiskEventProducerThread;
import com.reporting.mocks.process.intraday.IntradayRiskEventProducerThread;
import com.reporting.mocks.process.markets.MarketEventProducerThread;
import com.reporting.mocks.process.trades.TradePopulationProducerThread;

import java.util.Collection;
import java.util.UUID;

public class ProcessSimulator {
    protected UUID id;
    protected PricingGroupConfig config;
    protected MarketEventProducerThread marketEventProducerThread;
    protected IntradayRiskEventProducerThread intradayRiskEventProducerThread;
    protected RiskRunGeneratorThread riskRunGeneratorThread;


    protected ITradeStore tradeStore;
    protected ICalculationContextStore calculationContextStore;
    protected IMarketStore marketStore;
    protected TradeGenerator tradeGenerator;
    protected TradePopulationProducerThread tradePopulationProducerThread;

    protected RiskRunPublisher riskRunPublisher;

    ProcessEventQueues processEventQueues;

    protected ThreadGroup threadGroup;

    public ProcessSimulator(PricingGroupConfig config,
                            ApplicationConfig appConfig,
                            ICalculationContextStore calculationContextStore,
                            IMarketStore marketStore,
                            ITradeStore tradeStore) {
        this.id = UUID.randomUUID();
        this.config = config;
        this.tradeStore = tradeStore;
        this.calculationContextStore = calculationContextStore;
        this.marketStore = marketStore;
        this.tradeGenerator = new TradeGenerator(config.getTradeConfig());
        this.processEventQueues = new JavaProcessEventQueues();
        this.riskRunPublisher = new RiskRunResultKafkaPublisher(appConfig);
        //this.riskRunPublisher = new RiskRunResultQueuePublisher(this.processEventQueues.getRiskResultQueue());
    }

    public Collection<TradePopulation> getTradePopulations() {
        return this.tradeStore.getAllTradePopulation();
    }

    public TradePopulation getTradePopulation(TradePopulationId tradePopulationId) {
        return this.tradeStore.getTradePopulation(tradePopulationId);
    }

    public PricingGroup getPricingGroupId() {
        return this.config.getPricingGroupId();
    }

    protected void init() {
        if (this.threadGroup == null || this.threadGroup.isDestroyed()) {
            this.threadGroup = new ThreadGroup("PricingGroup: " + config.getPricingGroupId());


            RiskRunConsumerThread riskRunThread = new RiskRunConsumerThread(this.processEventQueues.getRiskResultQueue());
            new Thread(threadGroup, riskRunThread, "RiskRunConsumer").start();

            // RiskRunPublisher riskRunPublisher = new RiskRunResultQueuePublisher(this.processEventQueues.getRiskResultQueue());

            // RiskRunIgnitePublisher riskRunPublisher = new RiskRunIgnitePublisher(this.config.getPricingGroupId());


            //RiskRunResultKafkaPublisher riskRunPublisher = new RiskRunResultKafkaPublisher();

            this.riskRunGeneratorThread = new RiskRunGeneratorThread(
                    this.processEventQueues.getRiskRunRequestQueue(),
                    this.calculationContextStore,
                    this.tradeStore,
                    this.riskRunPublisher
            );

            new Thread(threadGroup, this.riskRunGeneratorThread, "RiskRunGeneratorThread").start();


            // kick-off end-of-day

            EndofDayRiskEventProducerThread eodThread = new EndofDayRiskEventProducerThread(
                    this.config.getPricingGroupId(),
                    this.config.getEndofdayConfig(),
                    this.tradeStore,
                    this.marketStore,
                    this.calculationContextStore,
                    this.processEventQueues.getRiskRunRequestQueue(),
                    this.riskRunPublisher);
            new Thread(threadGroup, eodThread, "EndofDayRiskEvent").start();

            // kick-off start-of-day

            // start intra-day process
            // initiate market environment change process
            this.marketEventProducerThread = new MarketEventProducerThread(
                    this.config.getPricingGroupId(),
                    this.marketStore,
                    this.riskRunPublisher,
                    this.config.getMarketPeriodicity(),
                    this.processEventQueues.getIntradayEventQueue());
            // start the market event thread
            new Thread(threadGroup, this.marketEventProducerThread, "MarketEventProducer").start();



            // initiate intra-day risk jobs
            this.intradayRiskEventProducerThread = new IntradayRiskEventProducerThread(
                    this.config.getPricingGroupId(),
                    this.config.getIntradayConfig(),
                    this.tradeStore,
                    this.marketStore,
                    this.calculationContextStore,
                    this.processEventQueues.getIntradayEventQueue(),
                    this.processEventQueues.getRiskRunRequestQueue(),
                    this.riskRunPublisher,
                    new MarketEnv(this.config.getPricingGroupId(), DataMarkerType.IND));

            new Thread(threadGroup, this.intradayRiskEventProducerThread, "IntradayRiskEvent").start();

            //TradeConfig tradeConfig, TradeStore tradeStore, BlockingQueue<Trade> tradeQueue
            this.tradePopulationProducerThread = new TradePopulationProducerThread(this.config.getTradeConfig(),
                    this.tradeStore,
                    this.tradeGenerator,
                    this.processEventQueues.getIntradayEventQueue());
            new Thread(threadGroup, this.tradePopulationProducerThread, "TradePopulationProducer").start();


        }
    }

    public void stop() {
        if (this.threadGroup != null && !this.threadGroup.isDestroyed()) {
            //this.threadGroup.interrupt();
            this.threadGroup.destroy();
        }
    }

    public void start() {
        this.start(this.config);
    }

    public void start(PricingGroupConfig config) {
        if (this.threadGroup == null || this.threadGroup.isDestroyed()) {
            if (this.config.getPricingGroupId() == config.getPricingGroupId()) {
                this.config = config;

                // initiate construction of initial trade population
                for (int i = 0; i < config.getTradeConfig().getStartingTradeCount(); i++) {
                    Trade newTrade = this.tradeGenerator.generateOneOtc();
                    this.tradeStore.add(newTrade);
                }

                this.init();
            }
        }
    }
}
