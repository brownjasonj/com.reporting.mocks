package com.reporting.mocks.model.id;

public class RiskRunId extends Id {
    public RiskRunId(String pricingGroupName) {
        super("/RiskRun/" + pricingGroupName);
    }
}
