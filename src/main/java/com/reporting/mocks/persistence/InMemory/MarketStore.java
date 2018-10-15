package com.reporting.mocks.persistence.InMemory;

import com.reporting.mocks.model.*;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.persistence.IMarketStore;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MarketStore implements IMarketStore {
    protected UUID storeId;
    protected PricingGroup pricingGroup;
    protected ConcurrentHashMap<UUID, MarketEnv> marketEnv;

    public MarketStore(PricingGroup pricingGroup) {
        this.pricingGroup = pricingGroup;
        this.marketEnv = new ConcurrentHashMap<>();
    }

    @Override
    public MarketEnv create(DataMarkerType type) {
        MarketEnv marketEnv = new MarketEnv(this.pricingGroup, type);
        this.marketEnv.put(marketEnv.getId().getId(), marketEnv);
        return marketEnv;
    }

    @Override
    public MarketEnv get(UUID id) {
        return this.marketEnv.get(id);
    }

    @Override
    public UUID getStoreId() {
        return storeId;
    }

    @Override
    public PricingGroup getPricingGroup() {
        return pricingGroup;
    }

    @Override
    public Collection<MarketEnv> getAll() {
        return this.marketEnv.values();
    }
}
