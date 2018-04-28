package com.reporting.mocks.model.risks;

public enum RiskType {
    PV("PV"),
    DELTA("Delta");

    protected String name;

    RiskType(String name) {
        this.name = name;
    }
}
