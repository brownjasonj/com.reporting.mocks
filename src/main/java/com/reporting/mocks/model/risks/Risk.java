package com.reporting.mocks.model.risks;

import java.util.UUID;

public abstract class Risk {
    protected UUID tcn;
    protected UUID riskRun;

    protected Risk(UUID riskRun, UUID tcn) {
        this.riskRun = riskRun;
        this.tcn = tcn;
    }

    public abstract RiskType getRiskType();

    public UUID getTcn() {
        return tcn;
    }

    public UUID getRiskRun() {
        return riskRun;
    }
}
