package com.reporting.mocks.model;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TradePopulation {
    protected UUID id;
    protected ConcurrentHashMap<UUID, Trade> trades;
    protected Date asOf;

    public TradePopulation(ConcurrentHashMap<UUID, Trade> trades) {
        this.id = UUID.randomUUID();
        this.trades = trades;
        this.asOf = new Date();
    }

    public UUID getId() {
        return this.id;
    }

    public Collection<Trade> getTrades() {
        return this.trades.values();
    }

    public int getTradeCount() {
        return this.trades.size();
    }

    public Date getAsOf() {
        return asOf;
    }

    public Trade getTrade(UUID id) {
        if (this.trades.containsKey(id)) {
            return this.trades.get(id);
        }
        else {
            return null;
        }
    }
}
