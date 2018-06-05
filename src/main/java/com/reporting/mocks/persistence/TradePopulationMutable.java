package com.reporting.mocks.persistence;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TradePopulationMutable extends TradePopulation {
    public TradePopulationMutable(String pricingGroupName, DataMarkerType type) {
        this.pricingGroupName = pricingGroupName;
        this.tradePopulationId = new TradePopulationId(pricingGroupName);
        this.tcnTrades = new ConcurrentHashMap<>();
        this.tradeTypeTrades = new ConcurrentHashMap<>();
        this.asOf = new Date();
        this.type = type;
    }

    public TradePopulationMutable(TradePopulationMutable tradePopulation, DataMarkerType type) {
        this.pricingGroupName = tradePopulation.pricingGroupName;
        this.tradePopulationId = new TradePopulationId(tradePopulation.pricingGroupName);
        this.tcnTrades = new ConcurrentHashMap<>(tradePopulation.tcnTrades);
        this.tradeTypeTrades = new ConcurrentHashMap<>();
        for(TradeType tradeType : tradePopulation.tradeTypeTrades.keySet()) {
            this.tradeTypeTrades.put(tradeType, new HashSet<>(tradePopulation.tradeTypeTrades.get(tradeType)));
        }
        this.asOf = new Date();
        this.type = type;
    }

    public Trade add(Trade trade) {
        Set<Trade> trades;
        if (!tradeTypeTrades.contains(trade.getTradeType())) {
            trades = new HashSet<>();
        }
        else {
            trades = tradeTypeTrades.get(trade.getTradeType());
        }
        tcnTrades.put(trade.getTcn(), trade);
        trades.add(trade);
        return trade;
    }

    public Trade oneAtRandom() {
        Collection<Trade> tradeCollection = tcnTrades.values();
        Optional<Trade> optionalTrade = tradeCollection.stream()
                .skip((int) (tradeCollection.size() * Math.random()))
                .findFirst();
        return optionalTrade.get();
    }

    public Trade delete(Tcn tcn) {
        if (this.tcnTrades.containsKey(tcn)) {
            Trade trade = this.tcnTrades.get(tcn);
            Set<Trade> trades = this.tradeTypeTrades.get(trade.getTradeType());
            if (trades.remove(trade))
                return this.tcnTrades.remove(tcn);
            else
                return null;
        }
        else
            return null;
    }
}
