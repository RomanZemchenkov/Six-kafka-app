package com.roman.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roman.service.dto.OrderEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class OrderEventDeserializer implements Deserializer<OrderEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OrderEvent deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, OrderEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
