package com.reporting.mocks.process.risks;

import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;

public class RiskRunRequest {
    protected CalculationContextId calculationId;
    protected MarketEnvId marketEnvId;
    protected TradePopulationId tradePopulationId;
    protected RiskRunId riskRunId;

    public RiskRunRequest(CalculationContextId calculationId,
                          MarketEnvId marketEnvId, TradePopulationId tradePopulationId) {
        this.calculationId = calculationId;
        this.marketEnvId = marketEnvId;
        this.tradePopulationId = tradePopulationId;
        this.riskRunId = new RiskRunId(calculationId.getPricingGroupName());
    }

    public CalculationContextId getCalculationId() {
        return calculationId;
    }

    public MarketEnvId getMarketEnvId() {
        return marketEnvId;
    }

    public TradePopulationId getTradePopulationId() {
        return tradePopulationId;
    }

    public RiskRunId getRiskRunId() {
        return riskRunId;
    }
}
