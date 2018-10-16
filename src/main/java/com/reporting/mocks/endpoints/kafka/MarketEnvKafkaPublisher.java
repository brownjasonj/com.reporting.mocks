package com.reporting.mocks.endpoints.kafka;

import com.google.gson.Gson;
import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;

public class MarketEnvKafkaPublisher {
    private String BOOTSTRAPSERVER =  "localhost:9092";
    private String TOPIC = "MarketEnv";
    private Properties kafkaProperties;
    private Producer producer;

    public MarketEnvKafkaPublisher(ApplicationConfig appConfig) {
        this.TOPIC = appConfig.getMarketEnvTopic();
        this.BOOTSTRAPSERVER = appConfig.getKafkaServer();

        this.kafkaProperties = new Properties();

        this.kafkaProperties.put("bootstrap.servers", this.BOOTSTRAPSERVER);
        this.kafkaProperties.put("key.serializer", "com.reporting.kafka.serialization.UUIDSerializer");
        this.kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<UUID,String>(this.kafkaProperties);
    }

    public void send(MarketEnv marketEnv) {
        Gson gson = new Gson();
        ProducerRecord<UUID, String> record = new ProducerRecord<>(this.TOPIC, marketEnv.getId().getId(), gson.toJson(marketEnv));
        try {
            this.producer.send(record).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
