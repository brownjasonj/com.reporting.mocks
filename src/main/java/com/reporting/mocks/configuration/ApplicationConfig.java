package com.reporting.mocks.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component("ApplicatonConfiguration")
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
}
