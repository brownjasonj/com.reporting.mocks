package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.MarketEnvId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MarketEnvRepository extends MongoRepository<MarketEnv, MarketEnvId> {
    MarketEnv getMarketEnvByMarketEnvId(MarketEnvId marketEnvId);
    List<MarketEnv> getMarketEnvsByPricingGroup(PricingGroup pricingGroup);
}
