package com.reporting.mocks.endpoints;

import com.reporting.mocks.process.risks.response.RiskRunResult;

public interface RiskRunPublisher {
    void send(RiskRunResult riskRunResult);
}
