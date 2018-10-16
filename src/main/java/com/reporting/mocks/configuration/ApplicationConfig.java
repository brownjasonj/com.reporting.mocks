package com.reporting.mocks.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
//@Scope(value= ConfigurableBeanFactory.SCOPE_SINGLETON)
@Scope
public class ApplicationConfig {
    @Autowired
    private Environment environment;

    /*
kafka:
  server: localhost:9092
  topic:
    intradayriskset: IntraDayRiskSet
    intradayrisktick: IntraDayRiskTick
    calccontext: CalculationContext
    market: Market
     */
    public String getKafkaServer() {
        return environment.getProperty("kafka.server");
    }

    public String getIntradayRiskSetTopic() {
        return environment.getProperty("kafka.topic.intradayriskset");
    }

    public String getIntradayRiskTickTopic() {
        return environment.getProperty("kafka.topic.intradayrisktick");
    }

    public String getCalculationContextTopic() {
        return environment.getProperty("kafka.topic.calccontext");
    }

    public String getIntradayTradeTopic() {
        return environment.getProperty("kafka.topic.intradaytrade");
    }

    public String getMarketEnvTopic() {
        return environment.getProperty("kafka.topic.market");
    }
}
