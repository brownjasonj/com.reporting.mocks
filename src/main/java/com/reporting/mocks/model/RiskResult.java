package com.reporting.mocks.model;

import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.Risk;

import java.util.List;

public class RiskResult {
    protected CalculationContextId calculationContextId;
    protected TradePopulationId tradePopulationId;
    protected RiskRunId riskRunId;
    protected int fragmentCount;
    protected int fragmentNo;
    protected List<Risk> results;

    public RiskResult(CalculationContextId calculationContextId, TradePopulationId tradePopulationId, RiskRunId riskRunId, int fragmentCount, int fragmentNo, List<Risk> results) {
        this.calculationContextId = calculationContextId;
        this.tradePopulationId = tradePopulationId;
        this.riskRunId = riskRunId;
        this.fragmentCount = fragmentCount;
        this.fragmentNo = fragmentNo;
        this.results = results;
    }


    public RiskResult(CalculationContextId calculationContextId, TradePopulationId tradePopulationId, RiskRunId riskRunId, List<Risk> results) {
        this(calculationContextId, tradePopulationId, riskRunId, 1, 0, results);
    }

    public CalculationContextId getCalculationContextId() {
        return calculationContextId;
    }

    public TradePopulationId getTradePopulationId() {
        return tradePopulationId;
    }

    public RiskRunId getRiskRunId() {
        return riskRunId;
    }

    public int getFragmentCount() {
        return fragmentCount;
    }

    public int getFragmentNo() {
        return fragmentNo;
    }

    public List<Risk> getResults() {
        return results;
    }
}
