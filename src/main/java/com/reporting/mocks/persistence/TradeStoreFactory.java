package com.reporting.mocks.persistence;

import com.reporting.mocks.model.PricingGroup;

import java.util.concurrent.ConcurrentHashMap;

public class TradeStoreFactory implements IPersistenceStoreFactory<TradeStore> {
    protected static TradeStoreFactory singleton = null;

    public static TradeStoreFactory get() {
        if (TradeStoreFactory.singleton == null)
            TradeStoreFactory.singleton = new TradeStoreFactory();
        return TradeStoreFactory.singleton;
    }

    protected ConcurrentHashMap<String, TradeStore> tradeStores;

    protected TradeStoreFactory() {
        this.tradeStores = new ConcurrentHashMap<>();
    }

    @Override
    public TradeStore create(String pricingGroupName) {
        TradeStore store = new TradeStore(pricingGroupName);
        this.tradeStores.put(store.getName(), store);
        return store;
    }

    @Override
    public TradeStore get(String name) {
        if (this.tradeStores.containsKey(name))
            return this.tradeStores.get(name);
        else
            return null;
    }

    @Override
    public TradeStore delete(String name) {
        if (this.tradeStores.containsKey(name))
            return this.tradeStores.remove(name);
        else
            return null;
    }
}
