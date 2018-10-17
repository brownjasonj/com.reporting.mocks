package com.reporting.mocks.endpoints;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.TradeLifecycle;

public interface RiskRunPublisher {
    void publish(CalculationContext calculationContext);
    void publish(MarketEnv marketEnv);
    void publishIntradayRiskRun(RiskResult riskResult);
    void publishIntradayTick(RiskResult riskResult);
    void publishIntradayTrade(TradeLifecycle tradeLifecycle);
    void publishEndofDayRiskRun(RiskResult riskResult);
}
