package com.reporting.mocks.endpoints.kafka;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.process.risks.response.MRRunResponse;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import com.reporting.mocks.process.risks.response.SRRunResponse;


public class RiskRunResultKafkaPublisher implements RiskRunPublisher {
    protected MRRiskResultKafkaProducer mrRisk;
    protected SRRiskResultKafkaProducer srRisk;

    public RiskRunResultKafkaPublisher() {
        this.mrRisk = new MRRiskResultKafkaProducer();
        this.srRisk = new SRRiskResultKafkaProducer();
    }

    @Override
    public void publish(RiskRunResult riskRunResult) {
        switch (riskRunResult.getSetKind()) {
            case MR:
                mrRisk.send((MRRunResponse)riskRunResult);
                break;
            case SR:
                srRisk.send((SRRunResponse)riskRunResult);
                break;
            default:
                break;
        }
    }

    @Override
    public void publish(CalculationContext calculationContext) {

    }

    @Override
    public void publish(MarketEnv marketEnv) {

    }
}
