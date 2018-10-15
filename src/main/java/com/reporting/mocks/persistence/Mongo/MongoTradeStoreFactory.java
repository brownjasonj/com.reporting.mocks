package com.reporting.mocks.persistence.Mongo;


import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.persistence.IPersistenceStoreFactory;
import com.reporting.mocks.persistence.ITradeStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope
public class MongoTradeStoreFactory implements IPersistenceStoreFactory<ITradeStore> {
    @Autowired
    protected TradePopulationRepository tradePopulationRepository;
    @Autowired
    protected TradeRepository tradeRepository;

    protected ConcurrentHashMap<String, ITradeStore> tradeStores;

    @Autowired
    protected MongoTradeStoreFactory(TradePopulationRepository tradePopulationRepository, TradeRepository tradeRepository) {
        this.tradeStores = new ConcurrentHashMap<>();
        this.tradePopulationRepository = tradePopulationRepository;
        this.tradeRepository = tradeRepository;
    }

    @Override
    public ITradeStore create(PricingGroup pricingGroupName) {
        ITradeStore store = new MongoTradeStore(pricingGroupName, this.tradePopulationRepository, this.tradeRepository);
        this.tradeStores.put(store.getPricingGroup().getName(), store);
        return store;
    }

    @Override
    public ITradeStore get(PricingGroup pricingGroup) {
        if (this.tradeStores.containsKey(pricingGroup.getName()))
            return this.tradeStores.get(pricingGroup.getName());
        else
            return null;
    }

    @Override
    public ITradeStore delete(PricingGroup pricingGroup) {
        if (this.tradeStores.containsKey(pricingGroup.getName()))
            return this.tradeStores.remove(pricingGroup.getName());
        else
            return null;
    }
}
