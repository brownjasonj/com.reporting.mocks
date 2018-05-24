package com.reporting.mocks.model.id;

public class MarketEnvId extends Id {
    public MarketEnvId(String pricingGroupName) {
        super("/MarketEnvironment/" + pricingGroupName);
    }
}
