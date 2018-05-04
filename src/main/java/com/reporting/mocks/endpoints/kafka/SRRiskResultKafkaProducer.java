package com.reporting.mocks.endpoints.kafka;

import com.reporting.mocks.process.risks.response.MRRunResponse;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import com.reporting.mocks.process.risks.response.SRRunResponse;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;

public class SRRiskResultKafkaProducer {
    private final String BOOTSTRAPSERVER =  "localhost:9092";
    private final String RISKRESULT = "srriskresult";
    private Properties kafkaProperties;
    private Producer producer;

    public SRRiskResultKafkaProducer() {
        this.kafkaProperties = new Properties();

        this.kafkaProperties.put("bootstrap.servers", this.BOOTSTRAPSERVER);
        this.kafkaProperties.put("key.serializer", "com.reporting.mocks.endpoints.kafka.UUIDSerializer");
        this.kafkaProperties.put("value.serializer", "com.reporting.mocks.endpoints.kafka.SRRiskRunResultSerializer");

        this.producer = new KafkaProducer<UUID,RiskRunResult>(this.kafkaProperties);
    }

    public void send(SRRunResponse riskResult) {
        ProducerRecord<UUID, SRRunResponse> record = new ProducerRecord<>(this.RISKRESULT, riskResult.getId(), riskResult);
        try {
            this.producer.send(record).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
