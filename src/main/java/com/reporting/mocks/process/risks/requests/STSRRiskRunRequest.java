package com.reporting.mocks.process.risks.requests;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.process.risks.RiskRunType;

public class STSRRiskRunRequest extends RiskRunRequest {
    protected Trade trade;
    protected RiskType riskType;

    public STSRRiskRunRequest(RiskRunType type, CalculationContext calculationContext, Trade trade, RiskType riskType) {
        super(type, calculationContext.getUri());
        this.trade = trade;
        this.riskType = riskType;
    }

    public Trade getTrade() {
        return trade;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    @Override
    public String toString() {
        return "{STSR, RiskType: " + riskType + " TCN: " + trade.getTcn() + "/" + trade.getVersion() + "}";
    }
}
