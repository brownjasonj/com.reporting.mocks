package com.reporting.mocks.endpoints.JavaQueue;

import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.Risk;

import java.util.concurrent.BlockingQueue;

public class IResultSetResultQueuePublisher implements IResultPublisher {
    protected BlockingQueue<RiskResultSet> riskQueue;

    public IResultSetResultQueuePublisher(BlockingQueue<RiskResultSet> riskQueue) {
        this.riskQueue= riskQueue;
    }

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
    public void publishIntradayRiskResultSet(RiskResultSet riskResultSet) {
        try {
            riskQueue.put(riskResultSet);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publishIntradayRiskResult(RiskResult<? extends Risk> riskResult) {

    }

    @Override
    public void publishEndOfDayRiskResultSet(RiskResultSet riskResultSet) {

    }

    @Override
    public void publishEndOfDayRiskResult(RiskResult<? extends Risk> riskResult) {

    }

    @Override
    public void publishStartOfDayRiskResultSet(RiskResultSet riskResultSet) {

    }

    @Override
    public void publishStartOfDayRiskResult(RiskResult<? extends Risk> riskResult) {

    }
}
