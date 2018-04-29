package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.Config;
import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.model.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradeGenerator {
    public static List<Trade> generator(Config config) {
        TradeConfig tradeConfig = config.getTradeConfig();
        ArrayList<Trade> trades = new ArrayList<Trade>();
        List<String> books = tradeConfig.getBooks();
        List<String> currency = tradeConfig.getCurrency();
        for(int i = 0; i < tradeConfig.getStartingTradeCount(); i++) {
            String book = books.get((new Random()).nextInt(books.size()));
            String ccy1 = currency.get((new Random()).nextInt(currency.size()));
            String ccy2 = currency.get((new Random()).nextInt(currency.size()));
            trades.add(new Trade(book, ccy1, ccy2));
        }
        return trades;
    }
}
