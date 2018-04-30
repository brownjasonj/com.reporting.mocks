package com.reporting.mocks.process.risks.requests;

import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.Trade;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.process.risks.RiskRunType;

public class STSRRiskRunRequest extends RiskRunRequest {
    protected Trade trade;
    protected RiskType riskType;

    public STSRRiskRunRequest(RiskRunType type, MarketEnv marketEnv, Trade trade, RiskType riskType) {
        super(type, marketEnv.getId());
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
