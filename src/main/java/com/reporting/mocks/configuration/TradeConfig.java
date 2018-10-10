package com.reporting.mocks.configuration;

import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.SecurityStatic;

import java.util.List;

public class TradeConfig {
    protected int startingTradeCount = 100;
    protected int newTradeStart = 0;
    protected int newTradePeriodicity = 1000;        // number of milliseconds between new tcnTrades (default: 10s)

    protected int modifiedTradeStart = 60 * 1000;
    protected int modifiedTradePeriodicity = 60 * 1000;    // number of milliseconds between trade modifications (default: 30s)

    protected int deleteTadeStart = 120 * 1000;
    protected int deleteTradePeriodicity = 120 * 1000;     // number of milliseconds between deletion of tcnTrades (default: 60s)

    List<String> books;
    UnderlyingConfig underlyings;
    List<TradeType> tradeTypes;

    public TradeConfig() {
    }

    public TradeConfig(List<String> books, UnderlyingConfig underlyings, List<TradeType> otcTradeTypes) {
        this.books = books;
        this.underlyings = underlyings;
        this.tradeTypes = otcTradeTypes;
    }

    public int getStartingTradeCount() {
        return startingTradeCount;
    }

    public void setStartingTradeCount(int startingTradeCount) {
        this.startingTradeCount = startingTradeCount;
    }

    public List<String> getBooks() {
        return books;
    }

    public UnderlyingConfig getUnderlyings() {
        return underlyings;
    }

    public int getNewTradePeriodicity() {
        return newTradePeriodicity;
    }

    public int getModifiedTradePeriodicity() {
        return modifiedTradePeriodicity;
    }

    public int getDeleteTradePeriodicity() {
        return deleteTradePeriodicity;
    }

    public int getNewTradeStart() {
        return newTradeStart;
    }

    public int getModifiedTradeStart() {
        return modifiedTradeStart;
    }

    public int getDeleteTadeStart() {
        return deleteTadeStart;
    }

    public List<TradeType> getTradeTypes() {
        return tradeTypes;
    }
}
