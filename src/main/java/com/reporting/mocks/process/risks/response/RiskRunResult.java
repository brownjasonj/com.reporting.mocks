package com.reporting.mocks.process.risks.response;

import com.reporting.mocks.process.risks.ResultKind;
import com.reporting.mocks.process.risks.requests.RiskRunRequest;

import java.util.Date;
import java.util.UUID;

public abstract class RiskRunResult {
    protected UUID id;
    protected Date date;
    protected ResultKind kind;
    protected int fragmentCount;
    protected int fragmentNo;
    protected RiskRunRequest request;


    public RiskRunResult(ResultKind kind, int fragmentCount, int fragmentNo, RiskRunRequest request) {
        this.id = UUID.randomUUID();
        this.request = request;
        this.date = new Date();
        this.kind = kind;
        this.fragmentCount = fragmentCount;
        this.fragmentNo = fragmentNo;

    }


    public UUID getId() {
        return id;
    }

    public RiskRunRequest getRequest() {
        return request;
    }

    public Date getDate() {
        return date;
    }

    public ResultKind getKind() {
        return kind;
    }

    public int getFragmentCount() {
        return fragmentCount;
    }

    public int getFragmentNo() {
        return fragmentNo;
    }
}
