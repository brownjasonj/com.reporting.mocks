package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.RiskResultSet;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RiskResultSetRepository extends MongoRepository<RiskResultSet,RiskRunId> {
    List<RiskResultSet> getAllByRiskRunId(RiskRunId riskRunId);
    List<RiskResultSet> getAllByCalculationContextId(CalculationContextId calculationContextId);
    List<RiskResultSet> getAllByTradePopulationId(TradePopulationId tradePopulationId);
}
