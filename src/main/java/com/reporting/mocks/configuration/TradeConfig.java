package com.reporting.mocks.configuration;

import java.util.List;

public class TradeConfig {
    int startingTradeCount = 100;
    int newTradePeriodicity = 10000;        // number of milliseconds between new trades (default: 10s)
    int modifiedTradePeriodicity = 30000;    // number of milliseconds between trade modifications (default: 30s)
    int deleteTradePeriodicity = 60000;     // number of milliseconds between deletion of trades (default: 60s)

    List<String> books;
    List<String> currency;

    public TradeConfig() {
    }

    public TradeConfig(List<String> books, List<String> currency) {
        this.books = books;
        this.currency = currency;
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

    public List<String> getCurrency() {
        return currency;
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
}
