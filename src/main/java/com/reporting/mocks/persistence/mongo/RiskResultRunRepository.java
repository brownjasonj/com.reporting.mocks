package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RiskResultRunRepository extends MongoRepository<RiskResult,RiskRunId> {
    List<RiskResult> getAllByRiskRunId(RiskRunId riskRunId);
    List<RiskResult> getAllByCalculationContextId(CalculationContextId calculationContextId);
    List<RiskResult> getAllByTradePopulationId(TradePopulationId tradePopulationId);
}
