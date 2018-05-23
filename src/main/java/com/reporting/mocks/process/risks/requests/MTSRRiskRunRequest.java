package com.reporting.mocks.process.risks.requests;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.process.risks.RiskRunType;

import java.util.UUID;

public class MTSRRiskRunRequest extends RiskRunRequest {
    protected UUID tradePopulationId;
    protected RiskType riskType;
    protected int fragmentSize;

    public MTSRRiskRunRequest(RiskRunType type, CalculationContext calculationContext, TradePopulation tradePop, RiskType riskType, int fragmentSize) {
        super(type, calculationContext.getUri());
        this.tradePopulationId = tradePop.getId();
        this.riskType = riskType;
        this.fragmentSize = fragmentSize;
    }

    public UUID getTradePopulationId() {
        return tradePopulationId;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }

    @Override
    public String toString() {
        return "{STSR, " + riskType + " popid: " + tradePopulationId + "}";
    }
}
