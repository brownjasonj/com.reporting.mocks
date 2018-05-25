package com.reporting.mocks.model.id;

import java.util.UUID;

public abstract class Id {
    private String uri;
    protected UUID id;

    public Id(String locator, UUID id) {
        if (id == null)
            this.id = UUID.randomUUID();
        else
            this.id = id;
        this.uri = locator +  "?id=" + this.getId();
    }

    public Id(String locator) {
        this(locator, UUID.randomUUID());
    }

    public UUID getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }
}
