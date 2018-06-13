package com.reporting.mocks.process.risks;

import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;

import java.util.List;

public class RiskRunRequest {
    protected RiskRunId riskRunId;
    protected RiskRunType riskRunType;
    protected CalculationContextId calculationId;
    protected MarketEnvId marketEnvId;
    protected TradePopulationId tradePopulationId;
    protected List<RiskType> risksToRun;
    protected Trade trade;

    public RiskRunRequest(RiskRunType riskRunType,
                          CalculationContextId calculationId,
                          MarketEnvId marketEnvId,
                          TradePopulationId tradePopulationId,
                          List<RiskType> risksToRun,
                          Trade trade) {
        this.riskRunId = new RiskRunId(calculationId.getPricingGroupName());
        this.riskRunType = riskRunType;
        this.calculationId = calculationId;
        this.marketEnvId = marketEnvId;
        this.tradePopulationId = tradePopulationId;
        this.risksToRun = risksToRun;
        this.trade = trade;
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

    public RiskRunType getRiskRunType() {
        return riskRunType;
    }

    public List<RiskType> getRisksToRun() {
        return risksToRun;
    }

    public boolean isSingleTrade() {
        return this.trade != null;
    }

    public Trade getTrade() {
        return trade;
    }
}
