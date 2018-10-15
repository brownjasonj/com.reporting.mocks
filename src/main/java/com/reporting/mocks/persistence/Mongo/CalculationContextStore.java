package com.reporting.mocks.persistence.Mongo;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.persistence.ICalculationContextStore;

import java.util.Collection;
import java.util.UUID;

public class CalculationContextStore implements ICalculationContextStore {
    protected PricingGroup pricingGroup;
    protected CalculationContextRepository calculationContextRepository;

    public CalculationContextStore(PricingGroup pricingGroup, CalculationContextRepository calculationContextRepository) {
        this.pricingGroup = pricingGroup;
        this.calculationContextRepository = calculationContextRepository;
    }

    @Override
    public CalculationContext create() {
        CalculationContext newCC = new CalculationContext(this.pricingGroup);
        return this.calculationContextRepository.save(newCC);
    }

    @Override
    public CalculationContext createCopy(CalculationContext calculationContextToCopy) {
        CalculationContext newCC = new CalculationContext(calculationContextToCopy);
        return this.calculationContextRepository.save(newCC);
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
