package com.reporting.mocks.model;

import java.util.Date;
import java.util.UUID;

public class MarketEnv {
    protected UUID id;
    protected Date asOf;

    public MarketEnv() {
        this.id = UUID.randomUUID();
        this.asOf = new Date();
    }

    public UUID getId() {
        return id;
    }

    public Date getAsOf() {
        return asOf;
    }
}
