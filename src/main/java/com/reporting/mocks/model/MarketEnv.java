package com.reporting.mocks.model;

import com.reporting.mocks.model.id.MarketEnvId;

import java.util.Date;

public class MarketEnv {
    protected MarketEnvId marketEnvId;
    protected Date asOf;
//    protected PricingGroup pricingGroup;
    protected DataMarkerType type;

    public MarketEnv(PricingGroup pricingGroup, DataMarkerType type) {
        this.marketEnvId= new MarketEnvId(pricingGroup.getName());
        this.asOf = new Date();
//        this.pricingGroup = pricingGroup;
        this.type = type;
    }

    public MarketEnvId getId() { return this.marketEnvId; }

    public Date getAsOf() {
        return asOf;
    }

//    public PricingGroup getPricingGroup() {
//        return pricingGroup;
//    }

    public DataMarkerType getType() {
        return type;
    }
}
