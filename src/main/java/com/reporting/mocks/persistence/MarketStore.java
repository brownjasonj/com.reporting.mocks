package com.reporting.mocks.persistence;

import com.reporting.mocks.model.*;
import com.reporting.mocks.model.id.MarketEnvId;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MarketStore {
    protected UUID storeId;
    protected PricingGroup pricingGroup;
    protected ConcurrentHashMap<MarketEnvId, MarketEnv> marketEnv;

    public MarketStore(PricingGroup pricingGroup) {
        this.pricingGroup = pricingGroup;
        this.marketEnv = new ConcurrentHashMap<>();
    }

    public MarketEnv create(DataMarkerType type) {
        MarketEnv marketEnv = new MarketEnv(this.pricingGroup, type);
        this.marketEnv.put(marketEnv.getId(), marketEnv);
        return marketEnv;
    }

    public MarketEnv get(MarketEnvId id) {
        return this.marketEnv.get(id);
    }

    public UUID getStoreId() {
        return storeId;
    }

    public PricingGroup getPricingGroup() {
        return pricingGroup;
    }

    public Collection<MarketEnv> getAll() {
        return this.marketEnv.values();
    }
}
