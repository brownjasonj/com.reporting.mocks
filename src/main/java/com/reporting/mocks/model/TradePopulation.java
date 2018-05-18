package com.reporting.mocks.model;

import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TradePopulation {
    protected UUID id;
    protected ConcurrentHashMap<Tcn, Trade> trades;
    protected Date asOf;
    protected DataMarkerType type;

    public TradePopulation(ConcurrentHashMap<Tcn, Trade> trades, DataMarkerType type) {
        this.type = type;
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

    public DataMarkerType getType() {
        return type;
    }

    public Date getAsOf() {
        return asOf;
    }

    public Trade getTrade(Tcn tcn) {
        if (this.trades.containsKey(tcn)) {
            return this.trades.get(tcn);
        }
        else {
            return null;
        }
    }
}
