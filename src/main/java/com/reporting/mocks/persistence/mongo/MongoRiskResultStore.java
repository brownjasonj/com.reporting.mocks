package com.reporting.mocks.persistence.mongo;

import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.persistence.IRiskResultStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope
public class MongoRiskResultStore implements IRiskResultStore {
    @Autowired
    RiskResultRepository riskResultRepository;

    @Override
    public List<RiskResult<? extends Risk>> getAll() {
        return this.riskResultRepository.findAll();
    }

    @Override
    public List<RiskResult<? extends Risk>> getAllByRiskRunId(RiskRunId riskRunId) {
        return this.riskResultRepository.getAllByRiskRunId(riskRunId);
    }

    @Override
    public List<RiskResult<? extends Risk>> getAllByTradePopulationId(TradePopulationId tradePopulationId) {
        return this.riskResultRepository.getAllByTradePopulationId(tradePopulationId);
    }

    @Override
    public RiskResult<? extends Risk> add(RiskResult<? extends Risk> riskResult) {
        return this.riskResultRepository.save(riskResult);
    }
}
