package com.reporting.mocks.endpoints.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;

public class MRRiskResultKafkaProducer {
//    public @Value("${kafka.mrresults.bootstrapserver}") String BOOTSTRAPSERVER;
//    public @Value("${kafka.mrresults.topic}") String RISKRESULT;
    private final String BOOTSTRAPSERVER =  "localhost:9092";
    private final String RISKRESULT = "mrriskresult";
    private Properties kafkaProperties;
    private Producer producer;

//    public MRRiskResultKafkaProducer() {
//        this.kafkaProperties = new Properties();
//
//        this.kafkaProperties.put("bootstrap.servers", this.BOOTSTRAPSERVER);
//        this.kafkaProperties.put("key.serializer", "com.reporting.mocks.endpoints.kafka.UUIDSerializer");
//        this.kafkaProperties.put("value.serializer", "com.reporting.mocks.endpoints.kafka.MRRiskRunResultSerializer");
//
//        this.producer = new KafkaProducer<UUID,RiskRunResult>(this.kafkaProperties);
//    }
//
//    public void send(MRRunResponse riskResult) {
//        ProducerRecord<UUID, MRRunResponse> record = new ProducerRecord<>(this.RISKRESULT, riskResult.getId(), riskResult);
//        try {
//            this.producer.send(record).get();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
