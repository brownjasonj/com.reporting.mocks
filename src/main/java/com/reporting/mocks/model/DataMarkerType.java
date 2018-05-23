package com.reporting.mocks.model;

public enum DataMarkerType {
    EOD("EOD"),
    SOD("SOD"),
    IND("IntraDay");

    String name;

    DataMarkerType(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
