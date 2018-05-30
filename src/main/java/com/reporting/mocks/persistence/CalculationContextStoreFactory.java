package com.reporting.mocks.persistence;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.PricingGroup;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CalculationContextStoreFactory {
    protected static ConcurrentHashMap<String, CalculationContextStore> stores;

    static {
        stores = new ConcurrentHashMap<>();
    }

    public static CalculationContextStore create(PricingGroup pricingGroup) {
        CalculationContextStore store = new CalculationContextStore(pricingGroup);
        stores.put(pricingGroup.getName(), store);
        return store;
    }

    public static CalculationContextStore get(String pricingGroupName) {
        if (stores.containsKey(pricingGroupName))
            return stores.get(pricingGroupName);
        else
            return null;
    }

    public static Enumeration<String> getStoreNames() {
        return stores.keys();
    }
}
