package com.roman.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.roman.service.dto.ResponseEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class KafkaListenerServiceIT extends KafkaPropertyConfigurationTest{

    @Autowired
    private KafkaListenerService kafkaListenerService;

    @Qualifier(value = "embeddedTestKafkaBroker")
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    @DisplayName("Testing the listen response event method")
    void listenEventMethodTesting() throws InterruptedException {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ResponseEventSerializer.class);

        KafkaProducer<String, ResponseEvent> producer = new KafkaProducer<>(producerProps);
        ResponseEvent event = new ResponseEvent("SEND", Instant.now());
        ProducerRecord<String, ResponseEvent> record = new ProducerRecord<>("order-status-topic", event);
        producer.send(record);
        producer.flush();

        Thread.sleep(300L);

    }


    @TestConfiguration
    static class EmbeddedKafkaConfiguration{

        @Bean("embeddedTestKafkaBroker")
        public EmbeddedKafkaBroker embeddedKafkaBroker(){
            return new EmbeddedKafkaZKBroker(1,true)
                    .kafkaPorts(19092);
        }
    }
}
