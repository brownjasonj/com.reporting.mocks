package com.reporting.mocks.configuration;

import com.reporting.mocks.model.risks.RiskType;

import java.util.ArrayList;
import java.util.List;

public class EndofDayConfig {
    protected List<RiskType> risks;

    public EndofDayConfig() {
        this.risks = new ArrayList<>();
    }

    public EndofDayConfig(List<RiskType> risks) {
        this.risks = risks;
    }

    public List<RiskType> getRisks() {
        return risks;
    }
}
