package com.reporting.mocks.model.id;

import java.util.UUID;

public class TradePopulationId extends Id {

    public TradePopulationId(String pricingGroupName, UUID id) {
        super("/TradePopulation/" + pricingGroupName, id);
    }

    public TradePopulationId(String pricingGroupName) {
        this(pricingGroupName, null);
    }
}
