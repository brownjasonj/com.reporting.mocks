package com.reporting.mocks.model.id;

public class TradePopulationId extends Id {
    public TradePopulationId(String pricingGroupName) {
        super("/TradePopulation/" + pricingGroupName);
    }
}
