package com.reporting.mocks.persistence;

import java.util.concurrent.ConcurrentHashMap;

public class MarketStoreFactory {
    protected static ConcurrentHashMap<String, MarketStore> stores;

    static {
        stores = new ConcurrentHashMap<>();
    }

    public static MarketStore create(String name) {
        MarketStore store = new MarketStore(name);
        stores.put(name, store);
        return store;
    }

    public static MarketStore get(String name) {
        if (stores.containsKey(name))
            return stores.get(name);
        else
            return null;
    }
}
