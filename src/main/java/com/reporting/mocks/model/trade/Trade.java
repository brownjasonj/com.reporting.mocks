package com.reporting.mocks.model.trade;

import com.reporting.mocks.model.underlying.Underlying;

import java.util.UUID;

public abstract class Trade {
    protected TradeKind kind;
    protected UUID tcn;
    protected int version;
    protected String book;
    protected TradeType tradeType;
    protected Double quantity;
    protected BuySell buySell;

    protected Trade(TradeKind kind, TradeType tradeType, UUID tcn, int version, String book) {
        this.kind = kind;
        this.tradeType = tradeType;
        this.tcn = tcn;
        this.version = version;
        this.book = book;
    }

    protected Trade(TradeKind kind, TradeType tradeType, String book) {
        this(kind, tradeType, UUID.randomUUID(), 0, book);
    }


    public Trade(Trade trade) {
        this(trade.getKind(), trade.getTradeType(), trade.getTcn(), trade.getVersion() + 1, trade.getBook());
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

    public TradeType getTradeType() {
        return tradeType;
    }

    public Double getQuantity() {
        return quantity;
    }

    public BuySell getBuySell() {
        return buySell;
    }

    public abstract Trade getNewVersion();

    @Override
    public String toString() {
        return "{Type: " + tradeType + ", Book: " + book + ", TCN: " + tcn + "/" + version + "}";
    }
}
