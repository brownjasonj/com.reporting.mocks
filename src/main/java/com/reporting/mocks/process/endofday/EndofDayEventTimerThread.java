package com.reporting.mocks.process.endofday;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.persistence.ITradeStore;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

public class EndofDayEventTimerThread extends TimerTask {
    protected BlockingQueue<TradePopulationId> tradePopulationIdQueue;
    protected ITradeStore tradeStore;

    public EndofDayEventTimerThread(ITradeStore tradeStore, BlockingQueue<TradePopulationId> tradePopulationIdQueue) {
        this.tradeStore = tradeStore;
        this.tradePopulationIdQueue = tradePopulationIdQueue;
    }

    @Override
    public void run() {
        TradePopulation tradePop = this.tradeStore.create(DataMarkerType.EOD);
        try {
            this.tradePopulationIdQueue.put(tradePop.getId());
        }
        catch (InterruptedException e) {
            System.out.println("Exception: EndofDayEventTimerThread : TradePopId " + tradePop.getId()+ " " + e);
        }
    }
}
