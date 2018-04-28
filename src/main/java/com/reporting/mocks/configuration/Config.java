package com.reporting.mocks.configuration;

import java.util.List;

public class Config {
    int startingTradeCount;
    int newTradePeriodicity;
    int modifiedTradePeriodicity;
    int deleteTradePeriodicity;

    List<String> books;
    List<String> currency;

    public Config(int count, List<String> books, List<String> currency) {
        this.startingTradeCount = count;
        this.books = books;
        this.currency = currency;
    }

    public int getStartingTradeCount() {
        return startingTradeCount;
    }

    public List<String> getBooks() {
        return books;
    }

    public List<String> getCurrency() {
        return currency;
    }
}
