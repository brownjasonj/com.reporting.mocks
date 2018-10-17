package com.reporting.mocks.persistence;

import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;

import java.util.List;

public interface IRiskResultStore {
    List<RiskResult> getAll();
    List<RiskResult> getAllByRiskRunId(RiskRunId riskRunId);
    List<RiskResult> getAllByCalculationContextId(CalculationContextId calculationContextId);
    List<RiskResult> getAllByTradePopulationId(TradePopulationId tradePopulationId);
    RiskResult add(RiskResult riskResult);
}
