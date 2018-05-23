package com.reporting.mocks.persistence;

import com.reporting.mocks.model.PricingGroup;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

public class MarketStoreFactory {
    protected static ConcurrentHashMap<URI, MarketStore> stores;

    static {
        stores = new ConcurrentHashMap<>();
    }

    public static MarketStore create(PricingGroup pricingGroup) {
        MarketStore store = new MarketStore(pricingGroup);
        stores.put(store.getStoreUri(), store);
        return store;
    }

    public static MarketStore get(String name) {
        if (stores.containsKey(name))
            return stores.get(name);
        else
            return null;
    }
}
