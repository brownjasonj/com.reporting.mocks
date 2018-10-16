package com.reporting.mocks.endpoints.kafka;

import com.google.gson.Gson;
import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.TradeLifecycle;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class TradeKafkaPublisher {
    private String BOOTSTRAPSERVER =  "localhost:9092";
    private String TOPIC = "IntraDayTrade";
    private Properties kafkaProperties;
    private Producer producer;

    public TradeKafkaPublisher(ApplicationConfig appConfig) {
        this.TOPIC = appConfig.getIntradayTradeTopic();
        this.BOOTSTRAPSERVER = appConfig.getKafkaServer();

        this.kafkaProperties = new Properties();

        this.kafkaProperties.put("bootstrap.servers", this.BOOTSTRAPSERVER);
        this.kafkaProperties.put("key.serializer", "com.reporting.kafka.serialization.UUIDSerializer");
        this.kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<Tcn,String>(this.kafkaProperties);
    }

    public void send(TradeLifecycle tradeLifecycle) {
        Gson gson = new Gson();
        ProducerRecord<Tcn, String> record = new ProducerRecord<>(this.TOPIC, tradeLifecycle.getTrade().getTcn(), gson.toJson(tradeLifecycle));
        try {
            this.producer.send(record).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
