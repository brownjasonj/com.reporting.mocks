package com.reporting.mocks.model;

import java.util.Date;
import java.util.UUID;

public class MarketEnv {
    protected UUID id;
    protected Date asOf;
    protected DataMarkerType type;

    public MarketEnv(DataMarkerType type) {
        this.id = UUID.randomUUID();
        this.asOf = new Date();
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public Date getAsOf() {
        return asOf;
    }
}
