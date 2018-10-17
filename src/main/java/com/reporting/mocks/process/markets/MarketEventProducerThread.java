package com.reporting.mocks.process.markets;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.persistence.IMarketStore;
import com.reporting.mocks.process.intraday.IntradayEvent;
import com.reporting.mocks.process.intraday.IntradayEventType;
import com.reporting.mocks.process.trades.TradePopulationProducerThread;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarketEventProducerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( MarketEventProducerThread.class.getName() );
    protected PricingGroup pricingGroup;
    protected IMarketStore marketStore;
    protected RiskRunPublisher riskPublisher;
    protected BlockingQueue<IntradayEvent<?>> marketEventQueue;
    protected int marketPeriodicity;
    protected boolean run = true;

    public MarketEventProducerThread(PricingGroup pricingGroup,
                                     IMarketStore marketStore,
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
            LOGGER.log( Level.FINE, "thread interrupted");
        }
    }
}
