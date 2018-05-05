package com.reporting.mocks.model;

import java.util.Date;
import java.util.UUID;

public class MarketEnv {
    protected UUID id;
    protected Date asOf;
    protected PricingGroup pricingGroup;
    protected DataMarkerType type;

    public MarketEnv(PricingGroup pricingGroup, DataMarkerType type) {
        this.id = UUID.randomUUID();
        this.asOf = new Date();
        this.pricingGroup = pricingGroup;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public Date getAsOf() {
        return asOf;
    }

    public PricingGroup getPricingGroup() {
        return pricingGroup;
    }

    public DataMarkerType getType() {
        return type;
    }
}
