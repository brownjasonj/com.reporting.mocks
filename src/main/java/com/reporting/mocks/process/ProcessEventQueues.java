package com.reporting.mocks.process;

import com.reporting.mocks.generators.process.streaming.RiskStreamMessage;
import com.reporting.mocks.model.RiskResultSet;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.process.risks.TradePopulationReactiveRiskRunRequest;
import com.reporting.mocks.process.risks.TradePopulationRiskRunRequest;

import java.util.concurrent.BlockingQueue;

public interface ProcessEventQueues {
    BlockingQueue<TradeLifecycle> getTradeLifecycleQueue();
    BlockingQueue<RiskResultSet> getRiskResultSetQueue();
    BlockingQueue<TradePopulationRiskRunRequest> getRiskRunRequestQueue();
    BlockingQueue<TradePopulationReactiveRiskRunRequest> getReactiveRiskRunRequestQueue();
    BlockingQueue<RiskStreamMessage<? extends Risk>> getRiskStreamMessageQueue();
}
