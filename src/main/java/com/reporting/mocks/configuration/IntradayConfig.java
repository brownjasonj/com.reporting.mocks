package com.reporting.mocks.configuration;

import com.reporting.mocks.model.risks.IntradayRiskType;

import java.util.List;

public class IntradayConfig {
    private List<IntradayRiskType> risks;
    private int periodicity;

    public IntradayConfig() {
    }

    public IntradayConfig(List<IntradayRiskType> risks, int periodicity) {
        this.risks = risks;
        this.periodicity = periodicity;
    }

    public List<IntradayRiskType> getRisks() {
        return this.risks;
    }

    public int getPeriodicity() {
        return this.periodicity;
    }
}
