package com.reporting.mocks.persistence;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.TradePopulation;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TradePopulationStore {
    protected String name;
    protected ConcurrentHashMap<UUID, TradePopulation> tradePopulation;

    public TradePopulationStore(String name) {
        this.tradePopulation = new ConcurrentHashMap<>();
    }

    public Collection<TradePopulation> getAllTradePopulation() {
        return this.tradePopulation.values();
    }


    public TradePopulation add(UUID id, TradePopulation tradePopulation) {
        this.tradePopulation.put(id, tradePopulation);
        return tradePopulation;
    }

    public TradePopulation get(UUID id) {
        if (this.tradePopulation.containsKey(id)) {
            return this.tradePopulation.get(id);
        }
        else {
            return null;
        }
    }
}
