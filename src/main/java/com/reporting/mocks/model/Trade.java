package com.reporting.mocks.model;

import java.util.UUID;

public class Trade {
    protected UUID tcn;
    protected int version;
    protected String book;
    protected String ccy1;
    protected String ccy2;
    protected String tradeType;


    protected Trade(UUID tcn, int version, String book, String ccy1, String ccy2) {
        this.tcn = tcn;
        this.version = version;
        this.book = book;
        this.ccy1 = ccy1;
        this.ccy2 = ccy2;
    }

    public Trade(Trade trade) {
        this(trade.getTcn(), trade.getVersion() + 1, trade.getBook(), trade.getCcy1(), trade.getCcy2());
    }

    public Trade(String book, String ccy1, String ccy2) {
        this(UUID.randomUUID(), 0, book, ccy1, ccy2);
    }

    public UUID getTcn() {
        return tcn;
    }

    public int getVersion() {
        return version;
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
