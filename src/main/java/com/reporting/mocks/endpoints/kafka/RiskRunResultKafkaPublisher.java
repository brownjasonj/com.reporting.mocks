package com.reporting.mocks.endpoints.kafka;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.model.trade.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class RiskRunResultKafkaPublisher implements RiskRunPublisher {
    protected RiskResultKafkaProducer riskResultProducer;
    protected CalculationContextKafkaProducer calculationContextProducer;
    protected TradeKafkaPublisher tradeKafkaPublisher;
    protected MarketEnvKafkaPublisher marketEnvKafkaPublisher;

    public RiskRunResultKafkaPublisher(ApplicationConfig appConfig) {
        this.riskResultProducer = new RiskResultKafkaProducer(appConfig);
        this.calculationContextProducer = new CalculationContextKafkaProducer(appConfig);
        this.tradeKafkaPublisher = new TradeKafkaPublisher(appConfig);
        this.marketEnvKafkaPublisher = new MarketEnvKafkaPublisher(appConfig);
    }

    @Override
    public void publish(CalculationContext calculationContext) {
        this.calculationContextProducer.send(calculationContext);
    }

    @Override
    public void publish(MarketEnv marketEnv) { this.marketEnvKafkaPublisher.send(marketEnv); }

    @Override
    public void publishIntradayRiskRun(RiskResult riskResult) {
        this.riskResultProducer.send(riskResult);
    }

    @Override
    public void publishIntradayTick(RiskResult riskResult) {
        this.riskResultProducer.send(riskResult);
    }

    @Override
    public void publishIntradayTrade(TradeLifecycle tradeLifecycle) { this.tradeKafkaPublisher.send(tradeLifecycle); }

    @Override
    public void publishEndofDayRiskRun(RiskResult riskResult) {

    }
}
