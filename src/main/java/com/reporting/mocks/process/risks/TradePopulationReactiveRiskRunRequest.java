package com.reporting.mocks.process.risks;

import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.RiskType;

import java.util.List;

public class TradePopulationReactiveRiskRunRequest extends TradePopulationRiskRunRequest{
    public TradePopulationReactiveRiskRunRequest(RiskRunType riskRunType, CalculationContextId calculationContextId, MarketEnvId marketEnvId, List<RiskType> risksToRun, TradePopulationId tradePopulationId) {
        super(riskRunType, calculationContextId, marketEnvId, risksToRun, tradePopulationId);
    }
}
