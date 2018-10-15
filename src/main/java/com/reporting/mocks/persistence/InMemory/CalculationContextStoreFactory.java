package com.reporting.mocks.persistence.InMemory;

import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.persistence.ICalculationContextStore;
import com.reporting.mocks.persistence.InMemory.CalculationContextStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class CalculationContextStoreFactory {
    protected ConcurrentHashMap<String, ICalculationContextStore> stores;

    public CalculationContextStoreFactory() {
        this.stores = new ConcurrentHashMap<>();
    }

    public ICalculationContextStore create(PricingGroup pricingGroup) {
        ICalculationContextStore store = new CalculationContextStore(pricingGroup);
        this.stores.put(pricingGroup.getName(), store);
        return store;
    }

    public ICalculationContextStore get(String pricingGroupName) {
        if (this.stores.containsKey(pricingGroupName))
            return stores.get(pricingGroupName);
        else
            return null;
    }

    public Enumeration<String> getStoreNames() {
        return this.stores.keys();
    }
}
