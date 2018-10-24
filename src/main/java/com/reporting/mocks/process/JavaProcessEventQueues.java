package com.reporting.mocks.process;

import com.reporting.mocks.generators.process.streaming.RiskStreamMessage;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.process.intraday.IntradayEvent;
import com.reporting.mocks.process.risks.RiskRunRequest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class JavaProcessEventQueues implements ProcessEventQueues {
    protected BlockingQueue<IntradayEvent<?>> intradayEventQueue;
    protected BlockingQueue<TradeLifecycle> tradeLifecycleQueue;
    protected BlockingQueue<RiskResult> riskResultQueue;
    protected BlockingQueue<RiskRunRequest> riskRunRequestQueue;
    protected BlockingQueue<RiskStreamMessage> riskStreamMessageQueue;

    public JavaProcessEventQueues() {
        this.intradayEventQueue = new ArrayBlockingQueue<>(4096);
        this.tradeLifecycleQueue = new ArrayBlockingQueue<>(4096);;
        this.riskResultQueue = new ArrayBlockingQueue<>(4096);
        this.riskResultQueue = new ArrayBlockingQueue<>(4096);
        this.riskRunRequestQueue = new ArrayBlockingQueue<>(4096);
        this.riskStreamMessageQueue = new ArrayBlockingQueue<>(4096);
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
    public BlockingQueue<RiskResult> getRiskResultQueue() {
        return this.riskResultQueue;
    }

    @Override
    public BlockingQueue<RiskRunRequest> getRiskRunRequestQueue() {
        return this.riskRunRequestQueue;
    }

    @Override
    public BlockingQueue<RiskStreamMessage> getRiskStreamMessageQueue() { return this.riskStreamMessageQueue; }
}
