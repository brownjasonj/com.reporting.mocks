package com.reporting.mocks.persistence.Mongo;

import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.MarketEnvId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface MarketEnvRepository extends MongoRepository<MarketEnv, MarketEnvId> {
    MarketEnv getMarketEnvByMarketEnvId(MarketEnvId marketEnvId);
    List<MarketEnv> getMarketEnvsByPricingGroup(PricingGroup pricingGroup);
}
