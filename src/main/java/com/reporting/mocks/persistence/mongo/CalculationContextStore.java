package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.persistence.ICalculationContextStore;

import java.util.Collection;
import java.util.UUID;

public class CalculationContextStore implements ICalculationContextStore {
    protected PricingGroup pricingGroup;
    protected CalculationContextRepository calculationContextRepository;
    protected CalculationContext currentCalculationContext;

    public CalculationContextStore(PricingGroup pricingGroup, CalculationContextRepository calculationContextRepository) {
        this.pricingGroup = pricingGroup;
        this.calculationContextRepository = calculationContextRepository;
        this.currentCalculationContext = null;
    }

    @Override
    public CalculationContext create() {
        return new CalculationContext(this.pricingGroup);
    }

    @Override
    public CalculationContext createCopy(CalculationContext calculationContextToCopy) {
        return new CalculationContext(calculationContextToCopy);
    }

    @Override
    public CalculationContext setCurrentContext(CalculationContext cc) {
        this.currentCalculationContext = cc;
        return this.calculationContextRepository.save(cc);
    }

    @Override
    public CalculationContext getCurrentContext() {
        return this.currentCalculationContext;
    }

    @Override
    public CalculationContext get(UUID id) {
        return this.calculationContextRepository.findCalculationContextByCalculationContextId(new CalculationContextId(this.pricingGroup.getName(), id));
    }

    @Override
    public PricingGroup getPricingGroup() {
        return this.pricingGroup;
    }

    @Override
    public Collection<CalculationContext> getAll() {
        return this.calculationContextRepository.findCalculationContextByPricingGroup(this.pricingGroup);
    }
}
