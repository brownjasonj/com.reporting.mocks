package com.reporting.mocks.persistence;

import com.reporting.mocks.model.CalculationContext;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CalculationContextStore {
    protected String name;
    protected ConcurrentHashMap<UUID, CalculationContext> calculationContexts;

    public CalculationContextStore(String name) {
        this.name = name;
        this.calculationContexts = new ConcurrentHashMap<>();
    }

    public String getName() {
        return name;
    }

    public CalculationContext add(CalculationContext calculationContext) {
        this.calculationContexts.put(calculationContext.getId(), calculationContext);
        return calculationContext;
    }

    public CalculationContext get(UUID id) {
        return this.calculationContexts.get(id);
    }

    public Collection<CalculationContext> getAll() {
        return this.calculationContexts.values();
    }
}
