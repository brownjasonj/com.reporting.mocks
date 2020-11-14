package com.reporting.mocks.generators;

import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

public interface IRiskGeneratorLite<R extends Risk> {
    public RiskType getRiskType();
    public R generate(MarketEnvId marketEnvId, Trade trade);
    public int calcTimeEstimate(TradeType tradeType);
}
