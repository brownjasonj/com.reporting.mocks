package com.reporting.mocks.model;

public enum RiskRunType {
    Intraday("Intraday"),
    EndOfDay("End of Day"),
    OnDemand("On Demand");

    protected String name;

    RiskRunType(String name) {
        this.name = name;
    }
}
