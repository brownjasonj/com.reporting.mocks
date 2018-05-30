package com.reporting.mocks.endpoints.kafka;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.RiskResult;

public class RiskRunResultKafkaPublisher implements RiskRunPublisher {
    protected RiskResultKafkaProducer riskResultProducer;

    public RiskRunResultKafkaPublisher() {
        this.riskResultProducer = new RiskResultKafkaProducer();
    }

    @Override
    public void publish(CalculationContext calculationContext) {

    }

    @Override
    public void publish(MarketEnv marketEnv) {

    }

    @Override
    public void publish(RiskResult riskResult) {
        this.riskResultProducer.send(riskResult);
    }
}
