package com.reporting.mocks.endpoints.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class MRRiskRunKafkaConsumer implements Runnable {
    private Properties kafkaProperties;
//    KafkaConsumer<UUID, MRRunResponse> consumer;


    public MRRiskRunKafkaConsumer() {
//        this.kafkaProperties = new Properties();
//
//        this.kafkaProperties.put("bootstrap.servers", "localhost:9092");
//        this.kafkaProperties.put("group.id", "test1");
//        this.kafkaProperties.put("key.deserializer", "com.reporting.mocks.endpoints.kafka.UUIDDeserializer");
//        this.kafkaProperties.put("value.deserializer", "com.reporting.mocks.endpoints.kafka.MRRiskRunResultDeserlializer");
//
//        // other properties that should be defined:
//        // input topic list
//        // input topic schemas (?)
//        // input topic serializer/deserializer
//        // output topic
//        // output topic schema (?)
//        // output topic serializer/deserializer
//        // number of worker instances in the group
//        // static service (name) list - look up via consul(?) or use zookeeper (?)
//        //
//
//        this.consumer = new KafkaConsumer<UUID, MRRunResponse>(this.kafkaProperties);
//        this.consumer.subscribe(Collections.singletonList("mrriskresult"));
    }

    public void consumer() {
//        try {
//            while (true) {
//                ConsumerRecords<UUID, MRRunResponse> records = this.consumer.poll(100);
//                for (ConsumerRecord<UUID, MRRunResponse> record : records)
//                {
//                    System.out.printf("MR topic = %s, partition = %s, offset = %d, Key = %s, Value = %s\n",
//                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
//                     MRRunResponse mrr = record.value();
//                    System.out.printf("Risk Count = %d\n", mrr.getRisks().size());
//                }
//            }
//        } finally {
//            consumer.close();
//        }

    }

    @Override
    public void run() {
        System.out.println("Kafka Consumer started");
        consumer();
    }
}
