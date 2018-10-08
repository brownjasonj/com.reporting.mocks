package com.reporting.mocks.process.risks;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;

public class RiskRequest {
    protected CalculationContext calculationContext;
    protected TradePopulationId tradePopulationId;
    protected RiskRunId riskRunId;

    public RiskRequest(CalculationContext calculationContext, TradePopulationId tradePopulationId) {
        this.calculationContext = calculationContext;
        this.tradePopulationId = tradePopulationId;
        this.riskRunId = new RiskRunId(this.calculationContext.getId().getPricingGroupName());
    }

    public CalculationContextId getCalculationId() {
        return this.calculationContext.getId();
    }

    public CalculationContext getCalculationContext() {
        return this.calculationContext;
    }

    public TradePopulationId getTradePopulationId() {
        return tradePopulationId;
    }

    public RiskRunId getRiskRunId() {
        return riskRunId;
    }
}
