package com.reporting.mocks.process;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.generators.TradeGenerator;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.persistence.TradeStore;
import com.reporting.mocks.persistence.TradeStoreFactory;
import com.reporting.mocks.process.intraday.IntradayEvent;
import com.reporting.mocks.process.intraday.IntradayRiskEventProducerThread;
import com.reporting.mocks.process.risks.RiskRunConsumerThread;
import com.reporting.mocks.process.trades.TradePopulationProducerThread;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CompleteProcess implements Runnable {
    protected static ConcurrentHashMap<String, CompleteProcess> processes;
    protected static ConcurrentHashMap<String, Thread> threads;

    static {
        CompleteProcess.processes = new ConcurrentHashMap<>();
        CompleteProcess.threads = new ConcurrentHashMap<>();
    }

    public static CompleteProcess addProcess(CompleteProcess completeProcess) {
        return CompleteProcess.processes.put(completeProcess.getPricingGroupName(), completeProcess);
    }

    public static CompleteProcess getProcess(String name) {
        if (CompleteProcess.processes.containsKey(name))
            return CompleteProcess.processes.get(name);
        else
            return null;
    }

    public static Thread startProcess(String name) {
        CompleteProcess cp = CompleteProcess.processes.get(name);
        if (cp != null) {
            if (CompleteProcess.threads.containsKey(name)) {
                return CompleteProcess.threads.get(name);
            }
            else {
                Thread thread = new Thread(cp);
                CompleteProcess.threads.put(name, thread);
                thread.start();
                return thread;
            }
        }
        else
            return null;
    }

    //
    // Instance definition
    //
    protected UUID id;
    protected PricingGroupConfig config;
    protected BlockingQueue<IntradayEvent<?>> intraDayEventQueue;
    protected MarketEventProducerThread marketEventProducerThread;
    protected IntradayRiskEventProducerThread intradayRiskEventProducerThread;

    protected TradeStore tradeStore;
    protected BlockingQueue<TradeLifecycle> tradeQueue;
    protected TradePopulationProducerThread tradePopulationProducerThread;

    public CompleteProcess(PricingGroupConfig config) {
        this.id = UUID.randomUUID();
        this.config = config;
        this.tradeStore = TradeStoreFactory.newTradeStore(config.getName());

    }

    public String getPricingGroupName() {
        return this.config.getName();
    }

    public Collection<TradePopulation> getTradePopulations() {
        return this.tradeStore.getAllTradePopulation();
    }

    public TradePopulation getTradePopulation(UUID tradePopulationId) {
        return this.tradeStore.getTradePopulation(tradePopulationId);
    }
    @Override
    public void run() {

        // initiate construction of initial trade population
        for(int i = 0; i < config.getTradeConfig().getStartingTradeCount(); i++) {
            this.tradeStore.putTrade(TradeGenerator.generateOne(config.getTradeConfig()));
        }

        // kick-off end-of-day

        // kick-off start-of-day

        // start intra-day process
        // initiate market environment change process
        this.intraDayEventQueue = new ArrayBlockingQueue(1024);
        this.marketEventProducerThread = new MarketEventProducerThread(this.config.getMarketPeriodicity(), this.intraDayEventQueue);
        // start the market event thread
        new Thread(this.marketEventProducerThread).start();


        BlockingQueue<RiskRunResult> riskResultQueue = new ArrayBlockingQueue<>(1024);

        RiskRunConsumerThread riskRunThread = new RiskRunConsumerThread(riskResultQueue);
        new Thread(riskRunThread).start();

        // initiate intra-day risk jobs
        this.intradayRiskEventProducerThread = new IntradayRiskEventProducerThread(this.config.getIntradayConfig(), this.tradeStore, this.intraDayEventQueue, riskResultQueue);
        new Thread(this.intradayRiskEventProducerThread).start();

        this.marketEventProducerThread.setRun(true);


        //TradeConfig tradeConfig, TradeStore tradeStore, BlockingQueue<Trade> tradeQueue
        this.tradePopulationProducerThread = new TradePopulationProducerThread(this.config.getTradeConfig(), this.tradeStore, this.intraDayEventQueue);
        new Thread(this.tradePopulationProducerThread).start();


    }
}
