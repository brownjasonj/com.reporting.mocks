package com.reporting.mocks.persistence.Mongo;

import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface TradePopulationRepository extends MongoRepository<TradePopulation, TradePopulationId> {
    TradePopulation getTradePopulationByTradePopulationId(TradePopulationId id);
}
