package com.reporting.mocks.model.id;

import java.util.UUID;

public class RiskRunId extends Id {

    public RiskRunId(String pricingGroupName, UUID id) {
        super("/RiskRun/" + pricingGroupName, id);
    }

    public RiskRunId(String pricingGroupName) {
        this(pricingGroupName, null);
    }
}
