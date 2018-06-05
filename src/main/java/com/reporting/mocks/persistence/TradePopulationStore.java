package com.reporting.mocks.persistence;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TradePopulationStore {
    protected String name;
    protected ConcurrentHashMap<UUID, TradePopulation> tradePopulation;

    public TradePopulationStore(String name) {
        this.name = name;
        this.tradePopulation = new ConcurrentHashMap<>();
    }

    public Collection<TradePopulation> getAllTradePopulation() {
        return this.tradePopulation.values();
    }


    public TradePopulation add(TradePopulation tradePopulation) {
        this.tradePopulation.put(tradePopulation.getId().getId(), tradePopulation);
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

    public List<TradePopulationId> getTradePopulationIds() {
        ArrayList<TradePopulationId> tradePopulationIds = new ArrayList<>();
        Enumeration<UUID> ids = this.tradePopulation.keys();
        while(ids.hasMoreElements()) {
            tradePopulationIds.add(new TradePopulationId(this.name, ids.nextElement()));
        }
        return tradePopulationIds;
    }
}
