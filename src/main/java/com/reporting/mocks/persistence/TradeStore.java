package com.reporting.mocks.persistence;

import com.reporting.mocks.model.Trade;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

    private TradeStore() {
        this.trades = new HashMap<>();
    }

    public Trade getTrade(UUID id) {
        if (this.trades.containsKey(id))
            return this.trades.get(id);
        else
            return null;
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

    public Collection<Trade> getAll() {
        return this.trades.values();
    }
}
