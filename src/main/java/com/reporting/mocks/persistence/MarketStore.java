package com.reporting.mocks.persistence;

import com.reporting.mocks.model.MarketEnv;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MarketStore {
    protected String name;
    protected ConcurrentHashMap<UUID, MarketEnv> marketEnv;

    public MarketStore(String name) {
        this.name = name;
        this.marketEnv = new ConcurrentHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void add(MarketEnv marketEnv) {
        this.marketEnv.put(marketEnv.getId(), marketEnv);
    }

    public MarketEnv get(UUID id) {
        return this.marketEnv.get(id);
    }

    public Collection<MarketEnv> getAll() {
        return this.marketEnv.values();
    }
}
