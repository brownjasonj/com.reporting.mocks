package com.reporting.mocks.process.trades;

import com.reporting.mocks.model.TradeLifecycleType;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

public class TradeEventTimerThread extends TimerTask {
    protected BlockingQueue<TradeLifecycleType> tradeEvent;
    protected TradeLifecycleType event;

    public TradeEventTimerThread(BlockingQueue<TradeLifecycleType> tradeEvent, TradeLifecycleType event) {
        this.tradeEvent = tradeEvent;
        this.event = event;
    }

    @Override
    public void run() {
        try {
            this.tradeEvent.put(this.event);
        }
        catch (InterruptedException e) {
            System.out.println("Exception: TradeEventTimerThread : " + this.event + " " + e);
        }
    }
}
