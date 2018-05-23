package com.reporting.mocks.process.risks.requests;

import com.reporting.mocks.process.risks.RiskRunType;

import java.net.URI;
import java.util.UUID;

public class RiskRunRequest {
    protected UUID id;
    protected RiskRunType type;
    protected URI calculationContextUri;


    protected RiskRunRequest() {

    }

    protected RiskRunRequest(RiskRunType type, URI calculationContextUri) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.calculationContextUri = calculationContextUri;
    }

    public UUID getId() {
        return id;
    }
    public RiskRunType getType() {
        return type;
    }
    public URI getCalculationContextUri() { return this.calculationContextUri; }
}
