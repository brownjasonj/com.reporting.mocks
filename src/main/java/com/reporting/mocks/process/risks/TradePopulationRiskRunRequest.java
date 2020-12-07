package com.reporting.mocks.process.risks;

import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;

import java.util.List;

public class TradePopulationRiskRunRequest {
    protected RiskRunId riskRunId;
    protected RiskRunType riskRunType;
    protected CalculationContextId calculationContextId;
    protected MarketEnvId marketEnvId;
    protected TradePopulationId tradePopulationId;
    protected List<RiskType> risksToRun;

    public TradePopulationRiskRunRequest(RiskRunType riskRunType,
                                         CalculationContextId calculationContextId,
                                         MarketEnvId marketEnvId,
                                         List<RiskType> risksToRun,
                                         TradePopulationId tradePopulationId) {
        this.riskRunId = new RiskRunId(marketEnvId.getPricingGroupName());
        this.riskRunType = riskRunType;
        this.calculationContextId = calculationContextId;
        this.marketEnvId = marketEnvId;
        this.tradePopulationId = tradePopulationId;
        this.risksToRun = risksToRun;
    }

    public CalculationContextId getCalculationContextId() {
        return calculationContextId;
    }

    public MarketEnvId getMarketEnvId() { return marketEnvId; }

    public TradePopulationId getTradePopulationId() {
        return tradePopulationId;
    }

    public RiskRunId getRiskRunId() {
        return riskRunId;
    }

    public RiskRunType getRiskRunType() {
        return riskRunType;
    }

    public List<RiskType> getRisksToRun() {
        return risksToRun;
    }
}
