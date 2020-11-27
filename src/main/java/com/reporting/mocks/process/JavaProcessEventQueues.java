package com.reporting.mocks.process;

import com.reporting.mocks.generators.process.streaming.RiskStreamMessage;
import com.reporting.mocks.model.RiskResultSet;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.process.risks.TradePopulationReactiveRiskRunRequest;
import com.reporting.mocks.process.risks.TradePopulationRiskRunRequest;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class JavaProcessEventQueues implements ProcessEventQueues {
    protected BlockingQueue<TradeLifecycle> tradeLifecycleQueue;
    protected BlockingQueue<RiskResultSet> riskResultSetQueue;
    protected BlockingQueue<TradePopulationRiskRunRequest> riskRunRequestQueue;
    protected BlockingQueue<TradePopulationReactiveRiskRunRequest> reactiveRiskRunRequestQueue;
    protected BlockingQueue<RiskStreamMessage<? extends Risk>> riskStreamMessageQueue;

    public JavaProcessEventQueues() {
        this.tradeLifecycleQueue = new LinkedBlockingQueue<>();
        this.riskResultSetQueue = new LinkedBlockingQueue<>();
        this.riskResultSetQueue = new LinkedBlockingQueue<>();
        this.riskRunRequestQueue = new LinkedBlockingQueue<>();
        this.reactiveRiskRunRequestQueue = new LinkedBlockingQueue<>();
        this.riskStreamMessageQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<TradePopulationReactiveRiskRunRequest> getReactiveRiskRunRequestQueue() { return this.reactiveRiskRunRequestQueue; }

    @Override
    public BlockingQueue<TradeLifecycle> getTradeLifecycleQueue() {
        return this.tradeLifecycleQueue;
    }

    @Override
    public BlockingQueue<RiskResultSet> getRiskResultSetQueue() {
        return this.riskResultSetQueue;
    }

    @Override
    public BlockingQueue<TradePopulationRiskRunRequest> getRiskRunRequestQueue() {
        return this.riskRunRequestQueue;
    }

    @Override
    public BlockingQueue<RiskStreamMessage<? extends Risk>> getRiskStreamMessageQueue() { return this.riskStreamMessageQueue; }
}
