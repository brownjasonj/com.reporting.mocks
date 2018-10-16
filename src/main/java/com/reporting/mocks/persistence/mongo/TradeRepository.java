package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface TradeRepository extends MongoRepository<Trade, UUID> {
    Trade getTradeByTcn(Tcn tcn);
}
