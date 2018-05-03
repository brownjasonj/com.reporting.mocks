package com.reporting.mocks.generators;

import com.reporting.mocks.process.risks.requests.RiskRunRequest;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;

public interface IRiskGenerator <R extends Risk> {
    public RiskType getRiskType();
    public R generate(RiskRunRequest riskRun, Trade trade);
}
