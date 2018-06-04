package com.reporting.mocks.endpoints.kafka;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.RiskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class RiskRunResultKafkaPublisher implements RiskRunPublisher {
    protected RiskResultKafkaProducer riskResultProducer;

    public RiskRunResultKafkaPublisher(ApplicationConfig appConfig) {
        this.riskResultProducer = new RiskResultKafkaProducer(appConfig);
    }

    @Override
    public void publish(CalculationContext calculationContext) {

    }

    @Override
    public void publish(MarketEnv marketEnv) {

    }

    @Override
    public void publishIntradayRiskRun(RiskResult riskResult) {
        this.riskResultProducer.send(riskResult);
    }

    @Override
    public void publishIntradayTick(RiskResult riskResult) {
        this.riskResultProducer.send(riskResult);
    }

    @Override
    public void publishEndofDayRiskRun(RiskResult riskResult) {

    }
}
