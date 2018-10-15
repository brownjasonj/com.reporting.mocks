package com.reporting.mocks.persistence.InMemory;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.ModelObjectUriGenerator;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.persistence.ICalculationContextStore;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CalculationContextStore implements ICalculationContextStore {
    protected PricingGroup pricingGroup;
    protected ConcurrentHashMap<UUID, CalculationContext> calculationContexts;

    public CalculationContextStore(PricingGroup pricingGroup) {
        this.pricingGroup = pricingGroup;
        this.calculationContexts = new ConcurrentHashMap<>();
    }


    @Override
    public CalculationContext create() {
        CalculationContext newCC = new CalculationContext(this.pricingGroup);
        this.calculationContexts.put(newCC.getCalculationContextId().getId(), newCC);
        return newCC;
    }

    @Override
    public CalculationContext createCopy(CalculationContext calculationContextToCopy) {
        CalculationContext newCC = new CalculationContext(calculationContextToCopy);
        this.calculationContexts.put(newCC.getCalculationContextId().getId(), newCC);
        return newCC;
    }

    @Override
    public CalculationContext get(UUID id) {
        return calculationContexts.get(id);
    }

    @Override
    public PricingGroup getPricingGroup() {
        return pricingGroup;
    }

    @Override
    public Collection<CalculationContext> getAll() {
        return this.calculationContexts.values();
    }
}
