package com.reporting.mocks.model.trade;

public enum TradeKind {
    Otc("OTC"),
    Security("Security");

    protected String name;

    TradeKind(String name) {
        this.name = name;
    }
}
