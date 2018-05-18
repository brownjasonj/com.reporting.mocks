package com.reporting.mocks.endpoints.JavaQueue;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.process.risks.response.RiskRunResult;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RiskRunResultQueuePublisher implements RiskRunPublisher {
    protected BlockingQueue<RiskRunResult> riskQueue;

    public RiskRunResultQueuePublisher(BlockingQueue<RiskRunResult> riskQueue) {
        this.riskQueue= riskQueue;
    }


    @Override
    public void publish(RiskRunResult riskRunResult) {
        try {
            riskQueue.put(riskRunResult);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(CalculationContext calculationContext) {

    }

    @Override
    public void publish(MarketEnv marketEnv) {

    }
}
