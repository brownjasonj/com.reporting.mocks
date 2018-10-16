package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.persistence.IMarketStore;

import java.util.Collection;
import java.util.UUID;

public class MongoMarketStore implements IMarketStore {
    protected PricingGroup pricingGroup;
    protected MarketEnvRepository marketEnvRepository;

    public MongoMarketStore(PricingGroup pricingGroup, MarketEnvRepository marketEnvRepository) {
        this.pricingGroup = pricingGroup;
        this.marketEnvRepository = marketEnvRepository;
    }
    @Override
    public MarketEnv create(DataMarkerType type) {
        MarketEnv marketEnv = new MarketEnv(this.pricingGroup, type);
        MarketEnv storedEnv = this.marketEnvRepository.save(marketEnv);
        return storedEnv;
    }

    @Override
    public MarketEnv get(UUID id) {
        return this.marketEnvRepository.getMarketEnvByMarketEnvId(new MarketEnvId(this.pricingGroup.getName(), id));
    }

    @Override
    public UUID getStoreId() {
        return null;
    }

    @Override
    public PricingGroup getPricingGroup() {
        return this.pricingGroup;
    }

    @Override
    public Collection<MarketEnv> getAll() {
        return this.marketEnvRepository.getMarketEnvsByPricingGroup(this.pricingGroup);
    }
}
