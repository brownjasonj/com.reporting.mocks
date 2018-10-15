package com.reporting.mocks.persistence.Mongo;

import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface TradeRepository extends MongoRepository<Trade, UUID> {
    Trade getTradeByTcn(Tcn tcn);
}
