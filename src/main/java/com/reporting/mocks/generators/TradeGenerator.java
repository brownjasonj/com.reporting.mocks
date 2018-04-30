package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.model.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradeGenerator {
    public static Trade generateOne(TradeConfig tradeConfig) {
        ArrayList<Trade> trades = new ArrayList<Trade>();
        List<String> books = tradeConfig.getBooks();
        List<String> currency = tradeConfig.getCurrency();
        String book = books.get((new Random()).nextInt(books.size()));
        String ccy1 = currency.get((new Random()).nextInt(currency.size()));
        String ccy2 = currency.get((new Random()).nextInt(currency.size()));
        return new Trade(book, ccy1, ccy2);
    }

    public static List<Trade> generateSet(TradeConfig tradeConfig, int count) {
        ArrayList<Trade> trades = new ArrayList<Trade>();
        for(int i = 0; i < count; i++) {
            trades.add(TradeGenerator.generateOne(tradeConfig));
        }
        return trades;
    }
}
