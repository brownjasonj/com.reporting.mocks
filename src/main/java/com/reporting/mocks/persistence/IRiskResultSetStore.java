package com.reporting.mocks.persistence;

import com.reporting.mocks.model.RiskResultSet;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;

import java.util.List;

public interface IRiskResultSetStore {
    List<RiskResultSet> getAll();
    List<RiskResultSet> getAllByRiskRunId(RiskRunId riskRunId);
    List<RiskResultSet> getAllByCalculationContextId(CalculationContextId calculationContextId);
    List<RiskResultSet> getAllByTradePopulationId(TradePopulationId tradePopulationId);
    RiskResultSet add(RiskResultSet riskResultSet);
}
