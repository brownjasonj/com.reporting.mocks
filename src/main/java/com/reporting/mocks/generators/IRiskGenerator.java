package com.reporting.mocks.generators;

import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.process.risks.RiskRequest;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.process.risks.RiskRunRequest;

public interface IRiskGenerator <R extends Risk> {
    public RiskType getRiskType();
    public R generate(RiskRequest riskRun, Trade trade);
    public int calcTimeEstimate(TradeType tradeType);
}
