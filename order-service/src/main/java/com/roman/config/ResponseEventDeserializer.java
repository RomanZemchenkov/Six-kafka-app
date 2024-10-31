package com.roman.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.roman.service.dto.ResponseEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class ResponseEventDeserializer implements Deserializer<ResponseEvent> {

    private final ObjectMapper objectMapper;

    public ResponseEventDeserializer() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ResponseEvent deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, ResponseEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
