package com.reporting.mocks.persistence;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

import java.util.*;

public class TradeStore  {
    protected String name;
    protected TradePopulationMutable currentTradePopulation;
    protected TradePopulationStore tradePopulationStore;


    public TradeStore(String name) {
        this.name = name;
        this.currentTradePopulation = new TradePopulationMutable(name, DataMarkerType.LIVE);
        this.tradePopulationStore = new TradePopulationStore(name);
    }

    public String getName() {
        return this.name;
    }

    public Trade add(Trade trade) {
        return this.currentTradePopulation.add(trade);
    }

    public Trade getTradeByTcn(Tcn tcn) {
        return this.currentTradePopulation.getTrade(tcn);
    }

    public Set<Trade> getByTradeType(TradeType tradeType) { return this.currentTradePopulation.getByTradeType(tradeType);}

    public Trade oneAtRandom() {
        return this.currentTradePopulation.oneAtRandom();
    }

    public Trade delete(Tcn tcn) {
        return this.currentTradePopulation.delete(tcn);
    }

    public TradePopulation create(DataMarkerType type) {
        TradePopulation tpm = new TradePopulationMutable(this.currentTradePopulation, type);
        this.tradePopulationStore.add(tpm);
        return tpm;
    }

    public TradePopulation getTradePopulation(UUID id) {
        return this.tradePopulationStore.get(id);
    }

    public Collection<TradePopulation> getAllTradePopulation() {
        return this.tradePopulationStore.getAllTradePopulation();
    }

    public List<TradePopulationId> getTradePopulationsIds() {
        return this.tradePopulationStore.getTradePopulationIds();
    }
}
