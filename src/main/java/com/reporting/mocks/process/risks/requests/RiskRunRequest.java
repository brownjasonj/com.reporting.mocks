package com.reporting.mocks.process.risks.requests;

import com.reporting.mocks.process.risks.RiskRunType;

import java.util.UUID;

public class RiskRunRequest {
    protected UUID id;
    protected RiskRunType type;
    protected UUID marketEnvId;


    protected RiskRunRequest() {

    }

    protected RiskRunRequest(RiskRunType type, UUID marketEnvId) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.marketEnvId = marketEnvId;
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
}
