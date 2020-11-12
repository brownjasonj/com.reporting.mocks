package com.reporting.mocks.process.trades;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.generators.TradeGenerator;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.model.TradeLifecycleType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.process.intraday.IntradayEvent;
import com.reporting.mocks.process.intraday.IntradayEventType;

public class TradePopulationProducerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( TradePopulationProducerThread.class.getName() );
    protected ITradeStore tradeStore;
    protected TradeGenerator tradeGenerator;
    protected BlockingQueue<TradeLifecycleType> tradeEventQueue;
    protected BlockingQueue<IntradayEvent<?>> intradayEventQueue;
    protected TradeConfig tradeConfig;
    protected IResultPublisher resultPublisher;

    public TradePopulationProducerThread(TradeConfig tradeConfig,
                                         ITradeStore tradeStore,
                                         TradeGenerator tradeGenerator,
                                         BlockingQueue<IntradayEvent<?>> intradayEventQueue,
                                         IResultPublisher resultPublisher) {
        this.tradeEventQueue = new LinkedBlockingDeque<>();
        this.tradeStore = tradeStore;
        this.tradeGenerator = tradeGenerator;
        this.intradayEventQueue = intradayEventQueue;
        this.tradeConfig = tradeConfig;
        this.resultPublisher = resultPublisher;
    }

    @Override
    public void run() {
        TimerTask newTradeTask = new TradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.New);
        TimerTask deleteTradeTask = new TradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.Delete);
        TimerTask modifiedTradeTask = new TradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.Modify);
        //running timer task as daemon thread
        Timer tradeTimer = new Timer(true);
        tradeTimer.schedule(newTradeTask, this.tradeConfig.getNewTradePeriodicity());

        Timer deleteTradeTimer = new Timer(true);
        deleteTradeTimer.schedule(deleteTradeTask, this.tradeConfig.getDeleteTradePeriodicity());

        Timer modifiedTradeTimer = new Timer(true);
        modifiedTradeTimer.schedule(modifiedTradeTask, this.tradeConfig.getModifiedTradePeriodicity());

        LOGGER.fine("**** This is just fine ****");
        try {
            while(true) {
                TradeLifecycleType tradeEvent = this.tradeEventQueue.take();
                switch (tradeEvent) {
                    case New:
                        int nextNewTrade  = (new Random()).nextInt(this.tradeConfig.getNewTradePeriodicity());
                        Trade newTrade = this.tradeGenerator.generateOneOtc();
                        TradeLifecycle newTradeLifecycle = new TradeLifecycle(tradeEvent, null, newTrade);
                        this.tradeStore.add(newTrade);
                        this.resultPublisher.publishIntradayTrade(newTradeLifecycle);
                        this.intradayEventQueue.put(new IntradayEvent<>(IntradayEventType.Trade, newTradeLifecycle));
                        tradeTimer.schedule(new TradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.New), nextNewTrade);
                        break;
                    case Modify:
                        int nextModifyTrade  = (new Random()).nextInt(this.tradeConfig.getModifiedTradePeriodicity());
                        Trade tradeToModify = this.tradeStore.oneAtRandom();
                        Trade modifiedTrade = tradeToModify.createNewVersion();
                        TradeLifecycle modifiedTradeLifecycle = new TradeLifecycle(tradeEvent, tradeToModify, modifiedTrade);
                        this.tradeStore.modified(tradeToModify, modifiedTrade);
                        this.resultPublisher.publishIntradayTrade(modifiedTradeLifecycle);
                        this.intradayEventQueue.put(new IntradayEvent<>(IntradayEventType.Trade, modifiedTradeLifecycle));
                        modifiedTradeTimer.schedule(new TradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.Modify), nextModifyTrade);
                        break;
                    case Delete:
                        int newDeleteTrade  = (new Random()).nextInt(this.tradeConfig.getModifiedTradePeriodicity());
                        Trade tradeToDelete = this.tradeStore.oneAtRandom();
                        TradeLifecycle deleteTradeLifecycle = new TradeLifecycle(tradeEvent, tradeToDelete, null);
                        this.tradeStore.delete(tradeToDelete.getTcn());
                        this.resultPublisher.publishIntradayTrade(deleteTradeLifecycle);
                        this.intradayEventQueue.put(new IntradayEvent<>(IntradayEventType.Trade, deleteTradeLifecycle));
                        deleteTradeTimer.schedule(new TradeEventTimerThread(this.tradeEventQueue, TradeLifecycleType.Delete), newDeleteTrade);
                        break;
                    default:
                        break;
                }
                // System.out.println("TradeEvent " + tradeEvent.toString());
            }
        } catch (InterruptedException e) {
            LOGGER.log( Level.FINE, "thread interrupted");
        }
    }
}
