package com.reporting.mocks.process.intraday;

import com.reporting.mocks.model.TradeLifecycleType;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

public class IntradayTradeEventTimerThread extends TimerTask {
    protected BlockingQueue<TradeLifecycleType> tradeEvent;
    protected TradeLifecycleType event;

    public IntradayTradeEventTimerThread(BlockingQueue<TradeLifecycleType> tradeEvent, TradeLifecycleType event) {
        this.tradeEvent = tradeEvent;
        this.event = event;
    }

    @Override
    public void run() {
        try {
            this.tradeEvent.put(this.event);
        } catch (InterruptedException e) {
            System.out.println("Exception: TradeEventTimerThread : " + this.event + " " + e);
        }
    }
}