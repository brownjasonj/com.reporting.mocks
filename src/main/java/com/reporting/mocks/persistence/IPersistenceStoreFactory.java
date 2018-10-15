package com.reporting.mocks.persistence;

import com.reporting.mocks.model.PricingGroup;

public interface IPersistenceStoreFactory<T> {
    public T create(PricingGroup pricingGroup);
    public T get(PricingGroup pricingGroup);
    public T delete(PricingGroup pricingGroup);
}
