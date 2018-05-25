package com.reporting.mocks.model.id;

import java.util.UUID;

public class CalculationContextId extends Id {
    private String pricingGroupName;

    public CalculationContextId(String pricingGroupName, UUID id) {
        super("/CalculationContext/" + pricingGroupName, id);
        this.pricingGroupName = pricingGroupName;
    }

    public CalculationContextId(String pricingGroupName){
        this(pricingGroupName, null);
    }

    public String getPricingGroupName() {
        return this.pricingGroupName;
    }
}
