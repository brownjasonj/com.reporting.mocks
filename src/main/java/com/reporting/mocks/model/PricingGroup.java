package com.reporting.mocks.model;

import java.util.UUID;

public class PricingGroup {
    protected UUID id;
    protected String name;

    public PricingGroup(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
