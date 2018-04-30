package com.reporting.mocks.persistence;

import java.util.concurrent.ConcurrentHashMap;

public class TradeStoreFactory {
    protected static ConcurrentHashMap<String, TradeStore> tradeStores;

    static {
        TradeStoreFactory.tradeStores = new ConcurrentHashMap<>();
    }

    public static TradeStore newTradeStore(String name) {
        TradeStore store = new TradeStore();
        TradeStoreFactory.tradeStores.put(name, store);
        return store;
    }

    public static TradeStore getTradeStore(String name) {
        if (TradeStoreFactory.tradeStores.containsKey(name))
            return TradeStoreFactory.tradeStores.get(name);
        else
            return null;
    }
}
