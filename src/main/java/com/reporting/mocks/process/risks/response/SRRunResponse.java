package com.reporting.mocks.process.risks.response;

import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.process.risks.ResultKind;
import com.reporting.mocks.process.risks.requests.RiskRunRequest;


public class SRRunResponse extends RiskRunResult {
    protected Risk risk;

    public SRRunResponse(ResultKind kind, RiskRunRequest request, Risk risk) {
        super(kind, 1, 0, request);
        this.risk = risk;
    }

    public Risk getRisk() {
        return risk;
    }
}
