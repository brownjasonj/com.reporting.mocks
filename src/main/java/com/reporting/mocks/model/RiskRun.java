package com.reporting.mocks.model;

import com.reporting.mocks.model.risks.RiskType;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class RiskRun {
    protected UUID id;
    protected RiskRunType type;
    protected Collection<Trade> trades;
    protected List<RiskType> risks;

    public RiskRun(RiskRunType type, Collection<Trade> trades, List<RiskType> risks) {
        this.type = type;
        this.id = UUID.randomUUID();
        this.trades = trades;
        this.risks = risks;
    }

    public UUID getId() {
        return id;
    }

    public Collection<Trade> getTrades() {
        return trades;
    }

    public List<RiskType> getRisks() {
        return risks;
    }
}
