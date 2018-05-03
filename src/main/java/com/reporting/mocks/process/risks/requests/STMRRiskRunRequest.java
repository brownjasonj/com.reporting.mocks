package com.reporting.mocks.process.risks.requests;

import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.process.risks.RiskRunType;

import java.util.List;

public class STMRRiskRunRequest extends RiskRunRequest {
    protected Trade trade;
    protected List<RiskType> riskTypes;
    protected int fragmentSize;

    public STMRRiskRunRequest(RiskRunType type, MarketEnv marketEnv, Trade trade, List<RiskType> riskTypes, int fragmentSize) {
        super(type, marketEnv.getId());
        this.trade = trade;
        this.riskTypes = riskTypes;
        this.fragmentSize = fragmentSize;
    }

    public Trade getTrade() {
        return trade;
    }

    public List<RiskType> getRiskTypes() {
        return riskTypes;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }
}
