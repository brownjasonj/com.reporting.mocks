package com.reporting.mocks.process;

import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.process.intraday.IntradayEvent;

import java.util.concurrent.BlockingQueue;

public interface ProcessEventQueues {
    BlockingQueue<IntradayEvent<?>> getIntradayEventQueue();
    BlockingQueue<TradeLifecycle> getTradeLifecycleQueue();
    BlockingQueue<RiskResult> getRiskResultQueue();

}
