package com.reporting.mocks.model;

import com.reporting.mocks.model.risks.Risk;

import java.util.List;

public class RiskRunResult {
    protected RiskRunRequest request;
    protected List<Risk> risks;

    public RiskRunResult(RiskRunRequest request, List<Risk> risks) {
        this.request = request;
        this.risks = risks;
    }

    public RiskRunRequest getRequest() {
        return request;
    }

    public List<Risk> getRisks() {
        return risks;
    }
}
