package com.reporting.mocks.model.id;

import java.util.UUID;

public class MarketEnvId extends Id {

    public MarketEnvId(String pricingGroupName, UUID id) {
        super("/MarketEnvironment/" + pricingGroupName, id);
    }
    public MarketEnvId(String pricingGroupName) {
        this(pricingGroupName, null);
    }
}
