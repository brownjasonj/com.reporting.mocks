package com.reporting.mocks.model.trade;

import java.util.UUID;

public class Tcn {
    protected UUID id;
    protected int version;

    public Tcn() {
        this.id = UUID.randomUUID();
        this.version = 0;
    }

    public Tcn(UUID id, int version) {
        this.id = id;
        this.version = version;
    }

    protected Tcn(Tcn tcn) {
        this(tcn.id, tcn.version + 1);
    }

    public Tcn getNewVersion() {
        return new Tcn(this);
    }

    public UUID getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return this.id + "." + this.version;
    }
}
