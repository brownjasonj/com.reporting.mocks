package com.reporting.mocks.endpoints;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.RiskResult;

public interface RiskRunPublisher {
    void publish(CalculationContext calculationContext);
    void publish(MarketEnv marketEnv);
    void publish(RiskResult riskResult);
}
