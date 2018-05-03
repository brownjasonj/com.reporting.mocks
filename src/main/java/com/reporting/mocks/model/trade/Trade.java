package com.reporting.mocks.model.trade;

import com.reporting.mocks.model.underlying.Underlying;

import java.util.UUID;

public abstract class Trade {
    protected TradeKind kind;
    protected UUID tcn;
    protected int version;
    protected String book;
    protected String tradeType;

    protected Trade(TradeKind kind, UUID tcn, int version, String book) {
        this.kind = kind;
        this.tcn = tcn;
        this.version = version;
        this.book = book;
    }

    protected Trade(TradeKind kind, String book) {
        this(kind, UUID.randomUUID(), 0, book);
    }


    public Trade(Trade trade) {
        this(trade.getKind(), trade.getTcn(), trade.getVersion() + 1, trade.getBook());
    }

    public abstract Underlying getUnderlying();

    public TradeKind getKind() {
        return kind;
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

    public String getTradeType() {
        return tradeType;
    }

    @Override
    public String toString() {
        return "{Book: " + book + " TCN: " + tcn + "/" + version + "}";
    }
}
