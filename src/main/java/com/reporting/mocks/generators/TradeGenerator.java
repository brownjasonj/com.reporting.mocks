package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.Config;
import com.reporting.mocks.model.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradeGenerator {
    public static List<Trade> generator(Config config) {
        ArrayList<Trade> trades = new ArrayList<Trade>();
        List<String> books = config.getBooks();
        List<String> currency = config.getCurrency();
        for(int i = 0; i < config.getStartingTradeCount(); i++) {
            String book = books.get((new Random()).nextInt(books.size()));
            String ccy1 = currency.get((new Random()).nextInt(currency.size()));
            String ccy2 = currency.get((new Random()).nextInt(currency.size()));
            trades.add(new Trade(book, ccy1, ccy2));
        }
        return trades;
    }
}
