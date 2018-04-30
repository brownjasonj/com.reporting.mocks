package com.reporting.mocks.persistence;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.Trade;
import com.reporting.mocks.model.TradePopulation;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TradeStore {
    protected ConcurrentHashMap<UUID, Trade> trades;
    protected ConcurrentHashMap<UUID, TradePopulation> tradePopulation;

    public TradeStore() {
        this.trades = new ConcurrentHashMap<>();
        this.tradePopulation = new ConcurrentHashMap<>();
    }


    public void putTrade(Trade t) {
        this.trades.put(t.getTcn(), t);
    }

    public Trade deleteTrade(UUID tcn) {
        if (this.trades.containsKey(tcn)) {
            return this.trades.remove(tcn);
        }
        else
            return null;
    }

    public Trade getTradeAtRandom() {
        Collection<Trade> tradeCollection = trades.values();
        Optional<Trade> optionalTrade = tradeCollection.stream()
                .skip((int) (tradeCollection.size() * Math.random()))
                .findFirst();
        return optionalTrade.get();
    }
    public Collection<TradePopulation> getAllTradePopulation() {
        return this.tradePopulation.values();
    }

    public TradePopulation getTradePopulation(DataMarkerType type) {
        TradePopulation tp = new TradePopulation(new ConcurrentHashMap<>(this.trades), type);
        this.tradePopulation.put(tp.getId(), tp);
        return tp;
    }

    public TradePopulation getTradePopulation(UUID id) {
        if (this.tradePopulation.containsKey(id)) {
            return this.tradePopulation.get(id);
        }
        else {
            return null;
        }
    }
}
