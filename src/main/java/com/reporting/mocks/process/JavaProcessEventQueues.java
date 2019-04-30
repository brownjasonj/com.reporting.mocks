package com.reporting.mocks.process;

import com.reporting.mocks.generators.process.streaming.RiskStreamMessage;
import com.reporting.mocks.model.RiskResultSet;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.process.intraday.IntradayEvent;
import com.reporting.mocks.process.risks.RiskRunRequest;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class JavaProcessEventQueues implements ProcessEventQueues {
    protected BlockingQueue<IntradayEvent<?>> intradayEventQueue;
    protected BlockingQueue<TradeLifecycle> tradeLifecycleQueue;
    protected BlockingQueue<RiskResultSet> riskResultSetQueue;
    protected BlockingQueue<RiskRunRequest> riskRunRequestQueue;
    protected BlockingQueue<RiskStreamMessage> riskStreamMessageQueue;

    public JavaProcessEventQueues() {
        this.intradayEventQueue = new LinkedBlockingQueue<>();
        this.tradeLifecycleQueue = new LinkedBlockingQueue<>();
        this.riskResultSetQueue = new LinkedBlockingQueue<>();
        this.riskResultSetQueue = new LinkedBlockingQueue<>();
        this.riskRunRequestQueue = new LinkedBlockingQueue<>();
        this.riskStreamMessageQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<IntradayEvent<?>> getIntradayEventQueue() {
        return this.intradayEventQueue;
    }

    @Override
    public BlockingQueue<TradeLifecycle> getTradeLifecycleQueue() {
        return this.tradeLifecycleQueue;
    }

    @Override
    public BlockingQueue<RiskResultSet> getRiskResultSetQueue() {
        return this.riskResultSetQueue;
    }

    @Override
    public BlockingQueue<RiskRunRequest> getRiskRunRequestQueue() {
        return this.riskRunRequestQueue;
    }

    @Override
    public BlockingQueue<RiskStreamMessage> getRiskStreamMessageQueue() { return this.riskStreamMessageQueue; }
}
