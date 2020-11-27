package com.reporting.mocks.process.endofday;

import com.reporting.mocks.interfaces.persistence.ITradePopulation;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.id.TradePopulationId;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EndofDayEventTimerThread extends TimerTask {
    private static final Logger LOGGER = Logger.getLogger( EndofDayEventTimerThread.class.getName() );

    protected BlockingQueue<TradePopulationId> tradePopulationIdQueue;
    protected ITradeStore tradeStore;

    public EndofDayEventTimerThread(ITradeStore tradeStore, BlockingQueue<TradePopulationId> tradePopulationIdQueue) {
        this.tradeStore = tradeStore;
        this.tradePopulationIdQueue = tradePopulationIdQueue;
    }

    @Override
    public void run() {
        ITradePopulation tradePop = this.tradeStore.createSnapShot(DataMarkerType.EOD);
        try {
            this.tradePopulationIdQueue.put(tradePop.getId());
        }
        catch (InterruptedException e) {
            LOGGER.log( Level.FINE, "thread interrupted");
        }
    }
}
