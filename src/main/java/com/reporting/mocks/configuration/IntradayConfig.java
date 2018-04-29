package com.reporting.mocks.configuration;

import com.reporting.mocks.model.risks.IntradayRiskType;

import java.util.List;

public class IntradayConfig {
    List<IntradayRiskType> risks;

    public IntradayConfig() {
    }

    public IntradayConfig(List<IntradayRiskType> risks) {
        this.risks = risks;
    }

    public List<IntradayRiskType> getRisks() {
        return risks;
    }
}
