package com.reporting.mocks.endpoints;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.process.risks.response.RiskRunResult;

public interface RiskRunPublisher {
    void publish(RiskRunResult riskRunResult);
    void publish(CalculationContext calculationContext);
    void publish(MarketEnv marketEnv);
}
