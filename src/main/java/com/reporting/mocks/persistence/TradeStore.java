package com.reporting.mocks.persistence;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TradeStore implements IPersistenceStore<Tcn, Trade> {
    protected String name;
    protected ConcurrentHashMap<Tcn, Trade> trades;
    protected ConcurrentHashMap<UUID, TradePopulation> tradePopulation;

    public TradeStore(String name) {
        this.name = name;
        this.trades = new ConcurrentHashMap<>();
        this.tradePopulation = new ConcurrentHashMap<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Trade add(Tcn tcn, Trade trade) {
        return this.trades.put(tcn, trade);
    }

    @Override
    public Trade get(Tcn tcn) {
        return this.trades.get(tcn);
    }

    @Override
    public Trade oneAtRandom() {
        Collection<Trade> tradeCollection = trades.values();
        Optional<Trade> optionalTrade = tradeCollection.stream()
                .skip((int) (tradeCollection.size() * Math.random()))
                .findFirst();
        return optionalTrade.get();
    }

    @Override
    public Collection<Trade> getAll() {
        return null;
    }

    @Override
    public Collection<Tcn> getKeys() {
        return null;
    }

    @Override
    public Trade delete(Tcn tcn) {
        if (this.trades.containsKey(tcn)) {
            return this.trades.remove(tcn);
        }
        else
            return null;
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
