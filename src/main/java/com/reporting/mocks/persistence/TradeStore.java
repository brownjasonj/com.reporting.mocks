package com.reporting.mocks.persistence;

import com.reporting.mocks.model.Trade;
import com.reporting.mocks.model.TradePopulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class TradeStore {

    private static TradeStore singletonStore;

    static {
        TradeStore.singletonStore = new TradeStore();
    }

    public static TradeStore getStore() {
        return TradeStore.singletonStore;
    }

    protected HashMap<UUID, Trade> trades;
    protected HashMap<UUID, TradePopulation> tradePopulation;

    private TradeStore() {
        this.trades = new HashMap<>();
        this.tradePopulation = new HashMap<>();
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

    public Collection<TradePopulation> getAllTradePopulation() {
        return this.tradePopulation.values();
    }

    public TradePopulation getTradePopulation() {
        TradePopulation tp = new TradePopulation(new HashMap<>(this.trades));
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
