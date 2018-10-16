package com.reporting.mocks.persistence;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

import java.util.Collection;
import java.util.List;

public interface ITradeStore {
    PricingGroup getPricingGroup();

    Trade add(Trade trade);

    Trade delete(Tcn tcn);

    Trade modified(Trade originalTrade, Trade modifyTrade);

    Trade getTradeByTcn(Tcn tcn);

    List<Trade> getByTradeType(TradeType tradeType);

    Trade oneAtRandom();

    TradePopulationMutable create(DataMarkerType type);

    TradePopulation getTradePopulation(TradePopulationId id);

    Collection<TradePopulation> getAllTradePopulation();

    List<TradePopulationId> getTradePopulationsIds();
}
