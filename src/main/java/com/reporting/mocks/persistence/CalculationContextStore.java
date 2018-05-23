package com.reporting.mocks.persistence;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.ModelObjectUriGenerator;
import com.reporting.mocks.model.PricingGroup;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CalculationContextStore {
    protected URI storeUri;
    protected PricingGroup pricingGroup;
    protected ConcurrentHashMap<UUID, CalculationContext> calculationContexts;

    public CalculationContextStore(PricingGroup pricingGroup) {
        this.pricingGroup = pricingGroup;
        this.storeUri = ModelObjectUriGenerator.getCalculationContextStoreURI(pricingGroup);
        this.calculationContexts = new ConcurrentHashMap<>();
    }

    public URI getName() {
        return this.storeUri;
    }

    public CalculationContext create() {
        CalculationContext newCC = new CalculationContext(this.pricingGroup);
        this.calculationContexts.put(newCC.getId(), newCC);
        return newCC;
    }

    public CalculationContext createCopy(CalculationContext calculationContextToCopy) {
        CalculationContext newCC = new CalculationContext(calculationContextToCopy);
        this.calculationContexts.put(newCC.getId(), newCC);
        return newCC;
    }

    public CalculationContext get(UUID id) {
        return calculationContexts.get(id);
    }

    public CalculationContext get(URI uri) {
        return this.calculationContexts.get(CalculationContext.getIdFromURI(uri));
    }

    public URI getStoreUri() {
        return storeUri;
    }

    public PricingGroup getPricingGroup() {
        return pricingGroup;
    }

    public Collection<CalculationContext> getAll() {
        return this.calculationContexts.values();
    }
}
