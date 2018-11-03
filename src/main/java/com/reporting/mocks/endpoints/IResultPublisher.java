package com.reporting.mocks.endpoints;

import com.reporting.mocks.model.*;

public interface IResultPublisher {
    void publish(CalculationContext calculationContext);
    void publish(MarketEnv marketEnv);
    void publishIntradayRiskResultSet(RiskResultSet riskResultSet);
    void publishIntradayRiskResult(RiskResult riskResult);
    void publishIntradayTrade(TradeLifecycle tradeLifecycle);
    void publishEndofDayRiskRun(RiskResultSet riskResultSet);
}
