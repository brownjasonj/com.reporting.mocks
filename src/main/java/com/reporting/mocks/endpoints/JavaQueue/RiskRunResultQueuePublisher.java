package com.reporting.mocks.endpoints.JavaQueue;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.RiskResult;

import java.util.concurrent.BlockingQueue;

public class RiskRunResultQueuePublisher implements RiskRunPublisher {
    protected BlockingQueue<RiskResult> riskQueue;

    public RiskRunResultQueuePublisher(BlockingQueue<RiskResult> riskQueue) {
        this.riskQueue= riskQueue;
    }


//    @Override
//    public void publish(RiskRunResult riskRunResult) {
//
//    }

    @Override
    public void publish(CalculationContext calculationContext) {

    }

    @Override
    public void publish(MarketEnv marketEnv) {

    }

    @Override
    public void publish(RiskResult riskResult) {
        try {
            riskQueue.put(riskResult);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
