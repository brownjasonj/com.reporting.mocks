package com.reporting.mocks.model;

import com.reporting.mocks.model.risks.RiskType;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class RiskRunRequest {
    UUID id;
    RiskRunType type;
    UUID marketEnvId;
    UUID tradePopulationId;
    protected List<RiskType> risks;

    public RiskRunRequest(RiskRunType type, MarketEnv marketEnv, TradePopulation tradePop, List<RiskType> risks) {
        this.type = type;
        this.id = UUID.randomUUID();
        this.tradePopulationId = tradePop.getId();
        this.marketEnvId = marketEnv.getId();
        this.risks = risks;
    }

    public UUID getId() {
        return id;
    }

    public RiskRunType getType() {
        return type;
    }

    public UUID getMarketEnvId() {
        return marketEnvId;
    }

    public UUID getTradePopulationId() {
        return tradePopulationId;
    }

    public List<RiskType> getRisks() {
        return risks;
    }
}
