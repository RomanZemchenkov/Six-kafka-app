package com.roman.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.roman.service.dto.ResponseEvent;
import org.apache.kafka.common.serialization.Serializer;


public class ResponseEventSerializer implements Serializer<ResponseEvent> {

    private final ObjectMapper objectMapper;

    public ResponseEventSerializer() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public byte[] serialize(String topic, ResponseEvent data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
