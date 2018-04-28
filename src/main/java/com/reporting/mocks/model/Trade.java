package com.reporting.mocks.model;

import java.util.UUID;

public class Trade {
    protected UUID tcn;

    public Trade() {
        this.tcn = UUID.randomUUID();
    }

    public UUID getTcn() {
        return tcn;
    }
}
