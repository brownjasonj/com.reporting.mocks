package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.model.trade.OtcTrade;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.underlying.OtcUnderlying;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradeGenerator {
    public static OtcTrade generateOne(TradeConfig tradeConfig) {
        ArrayList<Trade> trades = new ArrayList<Trade>();
        List<String> books = tradeConfig.getBooks();
        String book = books.get((new Random()).nextInt(books.size()));
        List<OtcUnderlying> underlyings = tradeConfig.getOtcUnderlying();
        return new OtcTrade(underlyings.get((new Random()).nextInt(underlyings.size())), book);
    }

    public static List<OtcTrade> generateSet(TradeConfig tradeConfig, int count) {
        ArrayList<OtcTrade> trades = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            trades.add(TradeGenerator.generateOne(tradeConfig));
        }
        return trades;
    }
}
