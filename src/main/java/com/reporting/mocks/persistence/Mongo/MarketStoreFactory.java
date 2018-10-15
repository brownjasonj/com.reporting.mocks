package com.reporting.mocks.persistence.Mongo;

import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.persistence.IMarketStore;
import com.reporting.mocks.persistence.IPersistenceStoreFactory;
import com.reporting.mocks.persistence.InMemory.MarketStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope
public class MarketStoreFactory implements IPersistenceStoreFactory<IMarketStore> {
    @Autowired
    protected MarketEnvRepository marketEnvRepository;
    protected ConcurrentHashMap<String, IMarketStore> stores;

    @Autowired
    public MarketStoreFactory(MarketEnvRepository marketEnvRepository) {
        this.marketEnvRepository = marketEnvRepository;
        this.stores = new ConcurrentHashMap<>();
    }

    @Override
    public IMarketStore get(PricingGroup pricingGroup) {
        if (this.stores.containsKey(pricingGroup.getName()))
            return stores.get(pricingGroup.getName());
        else
            return null;
    }

    @Override
    public IMarketStore delete(PricingGroup pricingGroup) {
        return null;
    }

    @Override
    public IMarketStore create(PricingGroup pricingGroup) {
        IMarketStore store = new MongoMarketStore(pricingGroup, this.marketEnvRepository);
        this.stores.put(pricingGroup.getName(), store);
        return store;
    }
}
