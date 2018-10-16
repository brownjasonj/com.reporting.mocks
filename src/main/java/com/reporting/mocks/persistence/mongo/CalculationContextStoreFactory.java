package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.persistence.ICalculationContextStore;
import com.reporting.mocks.persistence.IPersistenceStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope
public class CalculationContextStoreFactory implements IPersistenceStoreFactory<ICalculationContextStore> {
    @Autowired
    protected CalculationContextRepository calculationContextRepository;

    protected ConcurrentHashMap<String, ICalculationContextStore> stores;

    @Autowired
    public CalculationContextStoreFactory(CalculationContextRepository calculationContextRepository) {
        this.calculationContextRepository = calculationContextRepository;
        this.stores = new ConcurrentHashMap<>();
    }

    @Override
    public ICalculationContextStore create(PricingGroup pricingGroup) {
        ICalculationContextStore store = new CalculationContextStore(pricingGroup, this.calculationContextRepository);
        this.stores.put(pricingGroup.getName(), store);
        return store;
    }

    @Override
    public ICalculationContextStore get(PricingGroup pricingGroup) {
        if (this.stores.containsKey(pricingGroup.getName()))
            return stores.get(pricingGroup.getName());
        else
            return null;
    }

    @Override
    public ICalculationContextStore delete(PricingGroup pricingGroup) {
        if (this.stores.containsKey(pricingGroup.getName()))
            return this.stores.remove(pricingGroup.getName());
        else
            return null;
    }
}
