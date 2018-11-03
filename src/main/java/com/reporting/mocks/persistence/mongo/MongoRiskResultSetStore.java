package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.RiskResultSet;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.persistence.IRiskResultSetStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope
public class MongoRiskResultSetStore implements IRiskResultSetStore {
    @Autowired
    RiskResultSetRepository riskResultSetRepository;

    @Autowired
    public MongoRiskResultSetStore(RiskResultSetRepository riskResultSetRepository) {
        this.riskResultSetRepository = riskResultSetRepository;
    }

    @Override
    public List<RiskResultSet> getAll() {
        return riskResultSetRepository.findAll();
    }

    @Override
    public List<RiskResultSet> getAllByRiskRunId(RiskRunId riskRunId) {
        return this.riskResultSetRepository.getAllByRiskRunId(riskRunId);
    }

    @Override
    public List<RiskResultSet> getAllByCalculationContextId(CalculationContextId calculationContextId) {
        return this.riskResultSetRepository.getAllByCalculationContextId(calculationContextId);
    }

    @Override
    public List<RiskResultSet> getAllByTradePopulationId(TradePopulationId tradePopulationId) {
        return this.riskResultSetRepository.getAllByTradePopulationId(tradePopulationId);
    }

    @Override
    public RiskResultSet add(RiskResultSet riskResultSet) {
        return this.riskResultSetRepository.save(riskResultSet);
    }
}
