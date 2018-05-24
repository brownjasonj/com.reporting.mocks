package com.reporting.mocks.model.id;

public class CalculationContextId extends Id {
    private String pricingGroupName;

    public CalculationContextId(String pricingGroupName){
        super("/CalculationContextId/" + pricingGroupName);
        this.pricingGroupName = pricingGroupName;
    }

    public String getPricingGroupName() {
        return this.pricingGroupName;
    }
}
