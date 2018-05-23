package com.reporting.mocks.model;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

public class MarketEnv {
    protected UUID id;
    protected URI uri;
    protected Date asOf;
    protected PricingGroup pricingGroup;
    protected DataMarkerType type;

    public MarketEnv(PricingGroup pricingGroup, DataMarkerType type) {
        this.id = UUID.randomUUID();
        this.uri = ModelObjectUriGenerator.getMarketEnvURI(pricingGroup, type, id);
        this.asOf = new Date();
        this.pricingGroup = pricingGroup;
        this.type = type;
    }

    public UUID getId() { return this.id; }
    public URI getUri() {
        return uri;
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
