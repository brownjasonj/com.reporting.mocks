package com.reporting.mocks.persistence.InMemory;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.persistence.ITradeStore;
import com.reporting.mocks.persistence.TradePopulationMutable;

import java.util.*;

public class TradeStore implements ITradeStore {
    protected PricingGroup name;
    protected TradePopulationMutable currentTradePopulation;
    protected TradePopulationStore tradePopulationStore;


    public TradeStore(PricingGroup name) {
        this.name = name;
        this.currentTradePopulation = new TradePopulationMutable(name.getName(), DataMarkerType.LIVE);
        this.tradePopulationStore = new TradePopulationStore(name.getName());
    }

    @Override
    public PricingGroup getPricingGroup() {
        return this.name;
    }

    @Override
    public Trade add(Trade trade) {
        return this.currentTradePopulation.add(trade);
    }

    @Override
    public Trade delete(Tcn tcn) {
        return this.currentTradePopulation.delete(tcn);
    }

    @Override
    public Trade modified(Trade originalTrade, Trade modifyTrade) {
        Trade mod = this.add(modifyTrade);
        this.delete(originalTrade.getTcn());
        return mod;
    }

    @Override
    public Trade getTradeByTcn(Tcn tcn) {
        return this.currentTradePopulation.getTrade(tcn);
    }

    @Override
    public List<Trade> getByTradeType(TradeType tradeType) { return this.currentTradePopulation.getByTradeType(tradeType);}

    @Override
    public Trade oneAtRandom() {
        return this.currentTradePopulation.oneAtRandom();
    }



    @Override
    public TradePopulationMutable create(DataMarkerType type) {
        TradePopulationMutable tpm = new TradePopulationMutable(this.currentTradePopulation, type);
        this.tradePopulationStore.add(tpm);
        return tpm;
    }

    @Override
    public TradePopulation getTradePopulation(TradePopulationId id) {
        return this.tradePopulationStore.get(id.getId());
    }

    @Override
    public Collection<TradePopulation> getAllTradePopulation() {
        return this.tradePopulationStore.getAllTradePopulation();
    }

    @Override
    public List<TradePopulationId> getTradePopulationsIds() {
        return this.tradePopulationStore.getTradePopulationIds();
    }
}
