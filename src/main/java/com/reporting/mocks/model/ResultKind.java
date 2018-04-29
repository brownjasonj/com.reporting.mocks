package com.reporting.mocks.model;

public enum ResultKind {
    Complete("Complete"),
    Fragment("Fragment");

    protected String name;

    ResultKind(String name) {
        this.name = name;
    }
}
