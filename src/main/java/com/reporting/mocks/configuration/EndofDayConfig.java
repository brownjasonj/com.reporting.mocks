package com.reporting.mocks.configuration;

import com.reporting.mocks.model.risks.RiskType;

import java.util.ArrayList;
import java.util.List;

public class EndofDayConfig {
    protected List<RiskType> risks;
    protected int periodicity;

    public EndofDayConfig() {
        this.risks = new ArrayList<>();
        this.periodicity = 60 * 1000;
    }

    public EndofDayConfig(List<RiskType> risks, int periodicity) {
        this.risks = risks;
        this.periodicity = periodicity;
    }

    public List<RiskType> getRisks() {
        return risks;
    }

    public int getPeriodicity() {
        return periodicity;
    }
}
