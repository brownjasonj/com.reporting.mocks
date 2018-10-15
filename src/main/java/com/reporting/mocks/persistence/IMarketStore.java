package com.reporting.mocks.persistence;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;

import java.util.Collection;
import java.util.UUID;

public interface IMarketStore {
    MarketEnv create(DataMarkerType type);

    MarketEnv get(UUID id);

    UUID getStoreId();

    PricingGroup getPricingGroup();

    Collection<MarketEnv> getAll();
}
