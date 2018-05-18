package com.reporting.mocks.persistence;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class CalculationContextStoreFactory {
    protected static ConcurrentHashMap<String, CalculationContextStore> stores;

    static {
        stores = new ConcurrentHashMap<>();
    }

    public static CalculationContextStore create(String name) {
        CalculationContextStore store = new CalculationContextStore(name);
        stores.put(name, store);
        return store;
    }

    public static CalculationContextStore get(String name) {
        if (stores.containsKey(name))
            return stores.get(name);
        else
            return null;
    }

    public static Enumeration<String> getStoreNames() {
        return stores.keys();
    }
}
