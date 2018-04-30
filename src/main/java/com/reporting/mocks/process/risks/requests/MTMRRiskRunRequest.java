package com.reporting.mocks.process.risks.requests;

import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.process.risks.RiskRunType;

import java.util.List;
import java.util.UUID;

public class MTMRRiskRunRequest extends RiskRunRequest{
    protected UUID tradePopulationId;
    protected List<RiskType> riskTypes;
    protected int fragmentSize;

    public MTMRRiskRunRequest(RiskRunType type, MarketEnv marketEnv, TradePopulation tradePop, List<RiskType> riskTypes, int fragmentSize) {
        super(type, marketEnv.getId());
        this.tradePopulationId = tradePop.getId();
        this.riskTypes = riskTypes;
        this.fragmentSize = fragmentSize;
    }

    public UUID getTradePopulationId() {
        return tradePopulationId;
    }

    public List<RiskType> getRiskTypes() {
        return riskTypes;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }
}
