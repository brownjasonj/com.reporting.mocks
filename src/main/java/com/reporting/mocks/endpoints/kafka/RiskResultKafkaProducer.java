package com.reporting.mocks.endpoints.kafka;

import com.google.gson.Gson;
import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.model.RiskResult;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.UUID;

public class RiskResultKafkaProducer {
    private String BOOTSTRAPSERVER =  "localhost:9092";
    private String TOPIC = "RiskResult";
    private Properties kafkaProperties;
    private Producer producer;

    public RiskResultKafkaProducer(ApplicationConfig appConfig) {
        this.TOPIC = appConfig.getIntradayRiskSetTopic();
        this.BOOTSTRAPSERVER = appConfig.getKafkaServer();

        this.kafkaProperties = new Properties();

        this.kafkaProperties.put("bootstrap.servers", this.BOOTSTRAPSERVER);
        this.kafkaProperties.put("key.serializer", "com.reporting.kafka.serialization.UUIDSerializer");
        this.kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<UUID,String>(this.kafkaProperties);
    }

    public void send(RiskResult riskResult) {
        Gson gson = new Gson();
        ProducerRecord<UUID, String> record = new ProducerRecord<>(this.TOPIC, riskResult.getRiskRunId().getId(), gson.toJson(riskResult));
        try {
            this.producer.send(record).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
