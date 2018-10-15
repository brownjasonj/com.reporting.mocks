package com.reporting.mocks.persistence;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.PricingGroup;

import java.util.Collection;
import java.util.UUID;

public interface ICalculationContextStore {
    CalculationContext create();

    CalculationContext createCopy(CalculationContext calculationContextToCopy);

    CalculationContext get(UUID id);

    PricingGroup getPricingGroup();

    Collection<CalculationContext> getAll();
}
