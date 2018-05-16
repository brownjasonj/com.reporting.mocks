package com.reporting.mocks.model.trade;

public enum BuySell {
    Buy("Buy"),
    Sell("Sell");

    protected final String name;

    BuySell(String name) {
        this.name = name;
    }
}
