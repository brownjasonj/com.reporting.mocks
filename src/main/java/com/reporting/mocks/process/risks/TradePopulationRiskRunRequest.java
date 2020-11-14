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
    protected CalculationContextId calculationId;
    protected TradePopulationId tradePopulationId;
    protected List<RiskType> risksToRun;

    public TradePopulationRiskRunRequest(RiskRunType riskRunType,
                                         CalculationContextId calculationId,
                                         List<RiskType> risksToRun,
                                         TradePopulationId tradePopulationId) {
        this.riskRunId = new RiskRunId(calculationId.getPricingGroupName());
        this.riskRunType = riskRunType;
        this.calculationId = calculationId;
        this.tradePopulationId = tradePopulationId;
        this.risksToRun = risksToRun;
    }

    public CalculationContextId getCalculationId() {
        return calculationId;
    }

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
