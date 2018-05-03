package com.reporting.mocks.endpoints.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reporting.mocks.process.risks.response.MRRunResponse;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class MRRiskRunResultDeserlializer implements Deserializer<MRRunResponse> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public MRRunResponse deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        MRRunResponse riskRunResult = null;
        try {
            Gson gson = new GsonBuilder().create();
            String result  = mapper.readValue(data, String.class);
            riskRunResult = gson.fromJson(result, MRRunResponse.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return riskRunResult;
    }

    @Override
    public void close() {

    }
}
