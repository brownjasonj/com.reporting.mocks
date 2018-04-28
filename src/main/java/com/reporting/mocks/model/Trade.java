package com.reporting.mocks.model;

import java.util.UUID;

public class Trade {
    protected UUID tcn;
    protected String book;
    protected String ccy1;
    protected String ccy2;
    protected String tradeType;

    public Trade(String book, String ccy1, String ccy2) {
        this.tcn = UUID.randomUUID();
        this.book = book;
        this.ccy1 = ccy1;
        this.ccy2 = ccy2;
    }

    public UUID getTcn() {
        return tcn;
    }

    public String getBook() {
        return book;
    }

    public String getCcy1() {
        return ccy1;
    }

    public String getCcy2() {
        return ccy2;
    }

    public String getTradeType() {
        return tradeType;
    }
}
