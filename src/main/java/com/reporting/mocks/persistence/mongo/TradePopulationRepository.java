package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface TradePopulationRepository extends MongoRepository<TradePopulation, TradePopulationId> {
    TradePopulation getTradePopulationByTradePopulationId(TradePopulationId id);
}
