package com.reporting.mocks.generators;

import com.reporting.mocks.model.Trade;

import java.util.ArrayList;
import java.util.List;

public class TradeGenerator {
    public static List<Trade> generator(int count) {
        ArrayList<Trade> trades = new ArrayList<Trade>();
        for(int i = 0; i < count; i++) {
            trades.add(new Trade());
        }
        return trades;
    }
}
