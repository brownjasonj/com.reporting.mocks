package com.reporting.mocks.endpoints.kafka;

import com.reporting.mocks.model.TradePopulation;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;

public class TradePopulationKafkaProducer {
    private final String BOOTSTRAPSERVER =  "localhost:9092";
    private final String TRADEPOPULATUONTOPIC = "tradepopulation";
    private Properties kafkaProperties;
    private Producer producer;

    public TradePopulationKafkaProducer() {
        this.kafkaProperties = new Properties();

        this.kafkaProperties.put("bootstrap.servers", this.BOOTSTRAPSERVER);
        this.kafkaProperties.put("key.serializer", "com.reporting.mocks.endpoints.kafka.UUIDSerializer");
        this.kafkaProperties.put("value.serializer", "com.reporting.mocks.endpoints.kafka.RiskRunResult");

        this.producer = new KafkaProducer<UUID,String>(this.kafkaProperties);
    }

    public void sendMessage(TradePopulation tradePopulation) {
//        ProducerRecord<UUID, TradePopulation> record = new ProducerRecord<>(this.TRADEPOPULATUONTOPIC, tradePopulation.getId(), tradePopulation);
//        try {
//            this.producer.send(record).get();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
