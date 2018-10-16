package com.reporting.mocks.endpoints.JavaQueue;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.TradeLifecycle;

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
    public void publishIntradayTrade(TradeLifecycle tradeLifecycle) {

    }

    @Override
    public void publish(CalculationContext calculationContext) {

    }

    @Override
    public void publish(MarketEnv marketEnv) {

    }

    @Override
    public void publishIntradayRiskRun(RiskResult riskResult) {
        try {
            riskQueue.put(riskResult);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publishIntradayTick(RiskResult riskResult) {
        try {
            riskQueue.put(riskResult);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publishEndofDayRiskRun(RiskResult riskResult) {

    }
}
