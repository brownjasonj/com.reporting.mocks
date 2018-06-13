package com.reporting.mocks.process.risks;

public enum RiskRunType {
    Intraday("Intraday"),
    IntradayTick("IntradayTick"),
    EndOfDay("End of Day"),
    OnDemand("On Demand");

    protected String name;

    RiskRunType(String name) {
        this.name = name;
    }
}
