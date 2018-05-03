package com.reporting.mocks.endpoints.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reporting.mocks.process.risks.response.MRRunResponse;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class MRRiskRunResultSerializer implements Serializer<MRRunResponse> {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, MRRunResponse riskRunResult) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Gson gson = new GsonBuilder().create();
            retVal = objectMapper.writeValueAsString(gson.toJson(riskRunResult)).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}
