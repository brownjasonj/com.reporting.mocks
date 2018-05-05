package com.reporting.mocks.process;

import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.process.intraday.IntradayEvent;
import com.reporting.mocks.process.intraday.IntradayEventType;

import java.util.concurrent.BlockingQueue;

public class MarketEventProducerThread implements Runnable {
    protected PricingGroup pricingGroup;
    protected BlockingQueue<IntradayEvent<?>> marketEventQueue;
    protected int marketPeriodicity;
    protected boolean run = true;

    public MarketEventProducerThread(PricingGroup pricingGroup, int marketPeriodicity, BlockingQueue<IntradayEvent<?>> marketEventQueue) {
        this.pricingGroup = pricingGroup;
        this.marketPeriodicity = marketPeriodicity;
        this.marketEventQueue = marketEventQueue;
    }


    public boolean isRun() {
        System.out.println("MarketEventProducerThread created");
        return run;
    }

    public void setRun(boolean run) {
        System.out.println("MarketEventProducerThread created");
        this.run = run;
    }

    @Override
    public void run() {
        System.out.println("MarketEventProducerThread created");
        try {
            while(run)
            {
                this.marketEventQueue.put(new IntradayEvent<>(IntradayEventType.Market, new MarketEnv(this.pricingGroup, DataMarkerType.IND)));
                Thread.sleep(marketPeriodicity);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
