package com.reporting.mocks.model;

public enum MarketEnvType {
    EOD("EOD"),
    SOD("SOD"),
    IND("IntraDay");

    String name;

    MarketEnvType(String name) {
        this.name = name;
    }

}
