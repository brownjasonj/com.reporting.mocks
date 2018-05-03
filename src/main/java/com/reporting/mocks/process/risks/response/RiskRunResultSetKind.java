package com.reporting.mocks.process.risks.response;

public enum RiskRunResultSetKind {
    MR("MultiResult"),
    SR("Single Result");

    protected String name;

    RiskRunResultSetKind(String name) {
        this.name = name;
    }
}
