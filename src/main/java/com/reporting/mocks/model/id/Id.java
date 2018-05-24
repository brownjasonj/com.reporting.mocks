package com.reporting.mocks.model.id;

import java.util.UUID;

public abstract class Id {
    private String uri;
    protected UUID id;

    public Id(String locator) {
        this.id = UUID.randomUUID();
        this.uri = locator +  "?id=" + this.getId();
    }

    public UUID getId() {
        return id;
    }
}
