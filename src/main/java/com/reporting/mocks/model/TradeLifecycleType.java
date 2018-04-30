package com.reporting.mocks.model;

public enum TradeLifecycleType {
    New("New"),
    Delete("Delete"),
    Modify("Modify");

    String name;

    TradeLifecycleType(String name) {
        this.name = name;
    }
}
