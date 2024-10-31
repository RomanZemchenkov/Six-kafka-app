package com.roman.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.roman.service.dto.OrderEvent;
import com.roman.service.dto.ResponseEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:19092", "port=19092" })
public class KafkaPropertyConfigurationTest {

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry){
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:19092");
        registry.add("spring.kafka.admin.bootstrap-servers", () -> "localhost:19092");
    }

    public static class OrderEventDeserializer implements Deserializer<OrderEvent> {
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

    public static class ResponseEventSerializer implements Serializer<ResponseEvent> {
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
}
