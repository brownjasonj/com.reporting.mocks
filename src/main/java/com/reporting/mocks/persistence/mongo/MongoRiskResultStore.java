package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.persistence.IRiskResultStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope
public class MongoRiskResultStore implements IRiskResultStore {
    @Autowired
    RiskResultRunRepository riskResultRunRepository;

    @Autowired
    public MongoRiskResultStore (RiskResultRunRepository riskResultRunRepository) {
        this.riskResultRunRepository = riskResultRunRepository;
    }

    @Override
    public List<RiskResult> getAll() {
        return riskResultRunRepository.findAll();
    }

    @Override
    public List<RiskResult> getAllByRiskRunId(RiskRunId riskRunId) {
        return this.riskResultRunRepository.getAllByRiskRunId(riskRunId);
    }

    @Override
    public List<RiskResult> getAllByCalculationContextId(CalculationContextId calculationContextId) {
        return this.riskResultRunRepository.getAllByCalculationContextId(calculationContextId);
    }

    @Override
    public List<RiskResult> getAllByTradePopulationId(TradePopulationId tradePopulationId) {
        return this.riskResultRunRepository.getAllByTradePopulationId(tradePopulationId);
    }

    @Override
    public RiskResult add(RiskResult riskResult) {
        return this.riskResultRunRepository.save(riskResult);
    }
}
