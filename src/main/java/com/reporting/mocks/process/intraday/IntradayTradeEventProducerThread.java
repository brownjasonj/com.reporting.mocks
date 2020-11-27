package com.reporting.mocks.process.intraday;

import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.generators.TradeGenerator;
import com.reporting.mocks.interfaces.persistence.ITradePopulationLive;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.model.TradeLifecycleType;
import com.reporting.mocks.model.trade.Trade;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntradayTradeEventProducerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( IntradayTradeEventProducerThread.class.getName() );
    protected ITradeStore tradeStore;
    protected TradeGenerator tradeGenerator;
    protected BlockingQueue<TradeLifecycleType> tradeEventQueue;
    protected BlockingQueue<TradeLifecycle> intradayTradeLifeCycleEventQueue;
    protected TradeConfig tradeConfig;
    protected IResultPublisher resultPublisher;

    public IntradayTradeEventProducerThread(TradeConfig tradeConfig,
                                         ITradeStore tradeStore,
                                         TradeGenerator tradeGenerator,
                                         BlockingQueue<TradeLifecycle> intradayTradeLifeCycleEventQueue,
                                         IResultPublisher resultPublisher) {
        this.tradeEventQueue = new LinkedBlockingDeque<>();
        this.tradeStore = tradeStore;
        this.tradeGenerator = tradeGenerator;
        this.intradayTradeLifeCycleEventQueue = intradayTradeLifeCycleEventQueue;
        this.tradeConfig = tradeConfig;
        this.resultPublisher = resultPublisher;
    }


    @Override
    public void run() {
        //running timer task as daemon thread
        Timer tradeTimer = new Timer(true);
        tradeTimer.schedule(new IntradayTradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.New), this.tradeConfig.getNewTradePeriodicity());

        Timer deleteTradeTimer = new Timer(true);
        deleteTradeTimer.schedule(new IntradayTradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.Delete), this.tradeConfig.getDeleteTradePeriodicity());

        Timer modifiedTradeTimer = new Timer(true);
        modifiedTradeTimer.schedule(new IntradayTradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.Modify), this.tradeConfig.getModifiedTradePeriodicity());

        LOGGER.fine("**** This is just fine ****");
        try {
            ITradePopulationLive liveTrades = this.tradeStore.getLiveTradePopulation();
            while(true) {
                TradeLifecycleType tradeEvent = this.tradeEventQueue.take();
                switch (tradeEvent) {
                    case New:
                        int nextNewTrade  = (new Random()).nextInt(this.tradeConfig.getNewTradePeriodicity());
                        Trade newTrade = this.tradeGenerator.generateOneOtc();
                        TradeLifecycle newTradeLifecycle = new TradeLifecycle(tradeEvent, null, newTrade);
                        this.resultPublisher.publishIntradayTrade(newTradeLifecycle);
                        this.intradayTradeLifeCycleEventQueue.put(newTradeLifecycle);
                        tradeTimer.schedule(new IntradayTradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.New), nextNewTrade);
                        break;
                    case Modify:
                        int nextModifyTrade  = (new Random()).nextInt(this.tradeConfig.getModifiedTradePeriodicity());
                        Trade tradeToModify = liveTrades.oneAtRandom();
                        Trade modifiedTrade = tradeToModify.createNewVersion();
                        TradeLifecycle modifiedTradeLifecycle = new TradeLifecycle(tradeEvent, tradeToModify, modifiedTrade);
                        this.resultPublisher.publishIntradayTrade(modifiedTradeLifecycle);
                        this.intradayTradeLifeCycleEventQueue.put(modifiedTradeLifecycle);
                        modifiedTradeTimer.schedule(new IntradayTradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.Modify), nextModifyTrade);
                        break;
                    case Delete:
                        int newDeleteTrade  = (new Random()).nextInt(this.tradeConfig.getModifiedTradePeriodicity());
                        Trade tradeToDelete = liveTrades.oneAtRandom();
                        TradeLifecycle deleteTradeLifecycle = new TradeLifecycle(tradeEvent, tradeToDelete, null);
                        this.resultPublisher.publishIntradayTrade(deleteTradeLifecycle);
                        this.intradayTradeLifeCycleEventQueue.put(deleteTradeLifecycle);
                        deleteTradeTimer.schedule(new IntradayTradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.Delete), newDeleteTrade);
                        break;
                    default:
                        break;
                }
            }
        } catch (InterruptedException e) {
            LOGGER.log( Level.FINE, "thread interrupted");
        }
    }
}
