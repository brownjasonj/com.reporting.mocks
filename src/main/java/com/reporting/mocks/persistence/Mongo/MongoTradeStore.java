package com.reporting.mocks.persistence.Mongo;

import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.persistence.ITradeStore;
import com.reporting.mocks.persistence.TradePopulationMutable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MongoTradeStore implements ITradeStore {
    protected PricingGroup pricingGroup;
    protected TradePopulationMutable currentTradePopulation;
    protected TradePopulationRepository tradePopulationRepository;
    protected TradeRepository tradeRepository;

    public MongoTradeStore(PricingGroup pricingGroup, TradePopulationRepository tradePopulationRepository, TradeRepository tradeRepository) {
        this.pricingGroup = pricingGroup;
        this.currentTradePopulation = new TradePopulationMutable(pricingGroup.getName(), DataMarkerType.LIVE);
        this.tradeRepository = tradeRepository;
        this.tradePopulationRepository = tradePopulationRepository;
    }

    @Override
    public PricingGroup getPricingGroup() {
        return this.pricingGroup;
    }

    public void setPricingGroup(PricingGroup pricingGroup) {
        this.pricingGroup = pricingGroup;
    }

    @Override
    public Trade add(Trade trade) {
        this.tradeRepository.save(trade);
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
        this.tradePopulationRepository.save(tpm);
        return tpm;
    }

    @Override
    public TradePopulation getTradePopulation(TradePopulationId id) {
        return this.tradePopulationRepository.getTradePopulationByTradePopulationId(id);
    }

    @Override
    public Collection<TradePopulation> getAllTradePopulation() {
        return this.tradePopulationRepository.findAll();
    }

    @Override
    public List<TradePopulationId> getTradePopulationsIds() {
        return this.tradePopulationRepository.findAll().stream().map(tradePopulation -> tradePopulation.getId()).collect(Collectors.toList());
    }
}
