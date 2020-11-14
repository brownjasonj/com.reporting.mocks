package com.reporting.mocks.generators.process.streaming;

import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.process.risks.RiskRunType;

public class RiskStreamMessage<T extends Risk> {
    protected CalculationContextId calculationContextId;
    protected RiskRunId riskRunId;
    protected int riskCount;
    protected int riskNo;
    protected T risk;
    protected boolean isDelete;
    protected RiskRunType riskRunType;

    public RiskStreamMessage (
        CalculationContextId calculationContextId,
        RiskRunId riskRunId,
        RiskRunType riskRunType,
        int riskCount,
        int riskNo,
        T risk,
        boolean isDelete)
    {
        this.calculationContextId = calculationContextId;
        this.riskRunId = riskRunId;
        this.riskRunType = riskRunType;
        this.riskCount = riskCount;
        this.riskNo = riskNo;
        this.risk = risk;
        this.isDelete = isDelete;
    }

    public CalculationContextId getCalculationContextId() {
        return calculationContextId;
    }

    public RiskRunId getRiskRunId() {
        return riskRunId;
    }

    public RiskRunType getRiskRunType() {
        return riskRunType;
    }

    public int getRiskCount() {
        return riskCount;
    }

    public int getRiskNo() {
        return riskNo;
    }

    public T getRisk() {
        return risk;
    }

    public boolean isDelete() {
        return isDelete;
    }
}
