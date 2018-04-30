package com.reporting.mocks.model;

import com.reporting.mocks.model.risks.RiskType;

import java.util.UUID;

public class RiskRunRequest {
    protected UUID id;
    protected RiskRunType type;
    protected UUID marketEnvId;
    protected UUID tradePopulationId;
    protected RiskType riskType;
    protected int fragmentSize;

    public RiskRunRequest(RiskRunType type, MarketEnv marketEnv, TradePopulation tradePop, RiskType riskType, int fragmentSize) {
        this.type = type;
        this.id = UUID.randomUUID();
        this.tradePopulationId = tradePop.getId();
        this.marketEnvId = marketEnv.getId();
        this.riskType = riskType;
        this.fragmentSize = fragmentSize;
    }

    public UUID getId() {
        return id;
    }

    public int getFragmentSize() {
        return fragmentSize;
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

    public RiskType getRiskType() {
        return riskType;
    }
}
