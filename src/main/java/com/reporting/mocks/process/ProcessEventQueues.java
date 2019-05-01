package com.reporting.mocks.process;

import com.reporting.mocks.generators.process.streaming.RiskStreamMessage;
import com.reporting.mocks.model.RiskResultSet;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.process.intraday.IntradayEvent;
import com.reporting.mocks.process.risks.RiskRunRequest;

import java.util.concurrent.BlockingQueue;

public interface ProcessEventQueues {
    BlockingQueue<IntradayEvent<?>> getIntradayEventQueue();
    BlockingQueue<TradeLifecycle> getTradeLifecycleQueue();
    BlockingQueue<RiskResultSet> getRiskResultSetQueue();
    BlockingQueue<RiskRunRequest> getRiskRunRequestQueue();
    BlockingQueue<RiskStreamMessage<? extends Risk>> getRiskStreamMessageQueue();
}
