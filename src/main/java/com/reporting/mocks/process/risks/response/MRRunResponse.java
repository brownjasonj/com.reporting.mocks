package com.reporting.mocks.process.risks.response;

import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.process.risks.ResultKind;
import com.reporting.mocks.process.risks.requests.RiskRunRequest;

import java.util.List;

public class MRRunResponse extends RiskRunResult {
    protected List<Risk> risks;


    public MRRunResponse(ResultKind kind, int fragmentCount, int fragmentNo, RiskRunRequest request, List<Risk> risks) {
        super(kind, fragmentCount, fragmentNo, request);
        this.risks = risks;
    }

    public MRRunResponse(RiskRunRequest request, List<Risk> risks) {
        this(ResultKind.Complete, 1, 0, request, risks);
    }

    public List<Risk> getRisks() {
        return risks;
    }
}
