package com.reporting.mocks.process;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.persistence.MarketStore;
import com.reporting.mocks.process.intraday.IntradayEvent;
import com.reporting.mocks.process.intraday.IntradayEventType;

import javax.xml.crypto.Data;
import java.util.concurrent.BlockingQueue;

public class MarketEventProducerThread implements Runnable {
    protected PricingGroup pricingGroup;
    protected MarketStore marketStore;
    protected RiskRunPublisher riskPublisher;
    protected BlockingQueue<IntradayEvent<?>> marketEventQueue;
    protected int marketPeriodicity;
    protected boolean run = true;

    public MarketEventProducerThread(PricingGroup pricingGroup,
                                     MarketStore marketStore,
                                     RiskRunPublisher riskPublisher,
                                     int marketPeriodicity,
                                     BlockingQueue<IntradayEvent<?>> marketEventQueue) {
        this.pricingGroup = pricingGroup;
        this.marketStore = marketStore;
        this.riskPublisher = riskPublisher;
        this.marketPeriodicity = marketPeriodicity;
        this.marketEventQueue = marketEventQueue;
    }


    public boolean isRun() {
        return run;
    }

    @Override
    public void run() {
        System.out.println("MarketEventProducerThread created");
        try {
            while(run)
            {
                MarketEnv newMarket = this.marketStore.create(DataMarkerType.IND);
                this.marketEventQueue.put(new IntradayEvent<>(IntradayEventType.Market, newMarket));
                Thread.sleep(marketPeriodicity);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
