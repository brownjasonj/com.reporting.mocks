package com.reporting.mocks.process;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.JavaQueue.RiskRunResultQueuePublisher;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.endpoints.kafka.RiskRunResultKafkaPublisher;
import com.reporting.mocks.generators.TradeGenerator;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.persistence.*;
import com.reporting.mocks.process.endofday.EndofDayRiskEventProducerThread;
import com.reporting.mocks.process.intraday.IntradayRiskEventProducerThread;
import com.reporting.mocks.process.risks.RiskRunConsumerThread;
import com.reporting.mocks.process.trades.TradePopulationProducerThread;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CompleteProcess {
    protected static ConcurrentHashMap<String, CompleteProcess> processes;

    static {
        CompleteProcess.processes = new ConcurrentHashMap<>();
    }

    public static CompleteProcess addProcess(CompleteProcess completeProcess) {
        return CompleteProcess.processes.put(completeProcess.getPricingGroupId().getName(), completeProcess);
    }

    public static CompleteProcess getProcess(String name) {
        if (CompleteProcess.processes.containsKey(name))
            return CompleteProcess.processes.get(name);
        else
            return null;
    }

    //
    // Instance definition
    //
    protected UUID id;
    protected PricingGroupConfig config;
    protected MarketEventProducerThread marketEventProducerThread;
    protected IntradayRiskEventProducerThread intradayRiskEventProducerThread;

    protected TradeStore tradeStore;
    protected CalculationContextStore calculationContextStore;
    protected MarketStore marketStore;
    protected TradeGenerator tradeGenerator;
    protected TradePopulationProducerThread tradePopulationProducerThread;

    ProcessEventQueues processEventQueues;

    protected ThreadGroup threadGroup;

    public CompleteProcess(PricingGroupConfig config) {
        this.id = UUID.randomUUID();
        this.config = config;
        this.tradeStore = TradeStoreFactory.get().create(config.getPricingGroupId().getName());
        this.calculationContextStore = CalculationContextStoreFactory.create(config.getPricingGroupId());
        this.marketStore = MarketStoreFactory.create(config.getPricingGroupId());
        this.tradeGenerator = new TradeGenerator(config.getTradeConfig());
        this.processEventQueues = new JavaProcessEventQueues();
    }

    public Collection<TradePopulation> getTradePopulations() {
        return this.tradeStore.getAllTradePopulation();
    }

    public TradePopulation getTradePopulation(TradePopulationId tradePopulationId) {
        return this.tradeStore.get(tradePopulationId.getId());
    }

    public PricingGroup getPricingGroupId() {
        return this.config.getPricingGroupId();
    }

    protected void init() {
        if (this.threadGroup == null || this.threadGroup.isDestroyed()) {
            this.threadGroup = new ThreadGroup("PricingGroup: " + config.getPricingGroupId());


            RiskRunConsumerThread riskRunThread = new RiskRunConsumerThread(this.processEventQueues.getRiskResultQueue());
            new Thread(threadGroup, riskRunThread, "RiskRunConsumer").start();

            RiskRunPublisher riskRunPublisher = new RiskRunResultQueuePublisher(this.processEventQueues.getRiskResultQueue());

            // RiskRunIgnitePublisher riskRunPublisher = new RiskRunIgnitePublisher(this.config.getPricingGroupId());


            // RiskRunResultKafkaPublisher riskRunPublisher = new RiskRunResultKafkaPublisher();


            // kick-off end-of-day

            EndofDayRiskEventProducerThread eodThread = new EndofDayRiskEventProducerThread(
                    this.config.getPricingGroupId(),
                    this.config.getEndofdayConfig(),
                    tradeStore,
                    marketStore,
                    this.calculationContextStore,
                    riskRunPublisher);
            new Thread(threadGroup, eodThread, "EndofDayRiskEvent").start();

            // kick-off start-of-day

            // start intra-day process
            // initiate market environment change process
            this.marketEventProducerThread = new MarketEventProducerThread(
                    this.config.getPricingGroupId(),
                    this.marketStore,
                    riskRunPublisher,
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
                    riskRunPublisher,
                    new MarketEnv(this.config.getPricingGroupId(), DataMarkerType.IND));

            new Thread(threadGroup, this.intradayRiskEventProducerThread, "IntradayRiskEvent").start();

            //TradeConfig tradeConfig, TradeStore tradeStore, BlockingQueue<Trade> tradeQueue
            this.tradePopulationProducerThread = new TradePopulationProducerThread(this.config.getTradeConfig(), this.tradeStore, this.tradeGenerator, this.processEventQueues.getIntradayEventQueue());
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
                    this.tradeStore.add(newTrade.getTcn(), newTrade);
                }

                this.init();
            }
        }
    }
}