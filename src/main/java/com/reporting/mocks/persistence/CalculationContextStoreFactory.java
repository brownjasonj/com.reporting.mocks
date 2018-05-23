package com.reporting.mocks.persistence;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.PricingGroup;

import java.net.URI;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CalculationContextStoreFactory {
    protected static ConcurrentHashMap<URI, CalculationContextStore> stores;

    static {
        stores = new ConcurrentHashMap<>();
    }

    public static CalculationContextStore create(PricingGroup pricingGroup) {
        CalculationContextStore store = new CalculationContextStore(pricingGroup);
        stores.put(store.getStoreUri(), store);
        return store;
    }

    public static CalculationContextStore get(UUID id) {
        if (stores.containsKey(id))
            return stores.get(id);
        else
            return null;
    }

    public static CalculationContext get(URI ccUri) {
        try {
            URI path = new URI(ccUri.getPath());
            CalculationContextStore store = stores.get(path);
            CalculationContext cc = store.get(ccUri);
            return cc;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Enumeration<URI> getStoreNames() {
        return stores.keys();
    }
}
