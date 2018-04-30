package com.reporting.mocks.process.intraday;

public enum IntradayEventType {
    Market("Market"),
    Trade("Trade");

    String name;

    IntradayEventType(String name) {
        this.name = name;
    }
}
