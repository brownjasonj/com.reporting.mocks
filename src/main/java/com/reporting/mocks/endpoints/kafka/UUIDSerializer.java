package com.reporting.mocks.endpoints.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;
import java.util.UUID;

public class UUIDSerializer implements Serializer<UUID> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, UUID uuid) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Gson gson = new GsonBuilder().create();
            retVal = objectMapper.writeValueAsString(uuid.toString()).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}
