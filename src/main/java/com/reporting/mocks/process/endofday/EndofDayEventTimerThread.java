package com.reporting.mocks.process.endofday;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.persistence.TradeStore;

import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class EndofDayEventTimerThread extends TimerTask {
    protected BlockingQueue<TradePopulationId> tradePopulationIdQueue;
    protected TradeStore tradeStore;

    public EndofDayEventTimerThread(TradeStore tradeStore, BlockingQueue<TradePopulationId> tradePopulationIdQueue) {
        this.tradeStore = tradeStore;
        this.tradePopulationIdQueue = tradePopulationIdQueue;
    }

    @Override
    public void run() {
        TradePopulation tradePop = this.tradeStore.getTradePopulation(DataMarkerType.EOD);
        try {
            this.tradePopulationIdQueue.put(tradePop.getId());
        }
        catch (InterruptedException e) {
            System.out.println("Exception: EndofDayEventTimerThread : TradePopId " + tradePop.getId()+ " " + e);
        }
    }
}
