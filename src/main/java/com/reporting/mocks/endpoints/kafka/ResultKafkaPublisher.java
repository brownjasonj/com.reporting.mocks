package com.reporting.mocks.endpoints.kafka;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.endpoints.IResultPublisher;
import com.reporting.mocks.model.*;

public class ResultKafkaPublisher implements IResultPublisher {
    protected RiskResultSetKafkaProducer riskResultSetProducer;
    protected RiskResultKafkaProducer riskResultProducer;
    protected CalculationContextKafkaProducer calculationContextProducer;
    protected TradeKafkaPublisher tradeKafkaPublisher;
    protected MarketEnvKafkaPublisher marketEnvKafkaPublisher;

    public ResultKafkaPublisher(ApplicationConfig appConfig) {
        this.riskResultSetProducer = new RiskResultSetKafkaProducer(appConfig);
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
    public void publish(MarketEnv marketEnv) {
        this.marketEnvKafkaPublisher.send(marketEnv);
    }

    @Override
    public void publishIntradayRiskResultSet(RiskResultSet riskResultSet) {
        this.riskResultSetProducer.send(riskResultSet);
    }


    @Override
    public void publishIntradayRiskResult(RiskResult riskResult) { this.riskResultProducer.send(riskResult); }

    @Override
    public void publishIntradayTrade(TradeLifecycle tradeLifecycle) {
        this.tradeKafkaPublisher.send(tradeLifecycle);
    }

    @Override
    public void publishEndofDayRiskRun(RiskResultSet riskResultSet) {

    }
}
