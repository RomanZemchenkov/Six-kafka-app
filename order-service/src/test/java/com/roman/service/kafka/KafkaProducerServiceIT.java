package com.roman.service.kafka;

import com.roman.service.dto.Order;
import com.roman.service.dto.OrderEvent;
import com.roman.service.dto.ResponseEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
public class KafkaProducerServiceIT extends KafkaPropertyConfigurationTest{

    private final KafkaProducerService kafkaProducerService;

    private static final String ORDER_TOPIC_NAME = "order-topic";
    private static final String ORDER_STATUS_TOPIC_NAME = "order-status-topic";

    @Autowired
    @Qualifier(value = "embeddedKafkaBroker")
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry){
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:19092");
        registry.add("spring.kafka.admin.bootstrap-servers", () -> "localhost:19092");
    }


    @Autowired
    public KafkaProducerServiceIT(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Test
    @DisplayName("Testing the sendOrderMessage method with kafka")
    void sendOrderMessageMethodTest(){
        Order order = new Order("Product", 11);
        kafkaProducerService.sendOrderMessage(order);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("order-consumer-group", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderEventDeserializer.class);

        KafkaConsumer<String, OrderEvent> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList("order-topic"));
        ConsumerRecords<String, OrderEvent> resultRecords = consumer.poll(Duration.of(5L, ChronoUnit.SECONDS));
        Iterator<ConsumerRecord<String, OrderEvent>> orderTopicIterator = resultRecords.records(ORDER_TOPIC_NAME).iterator();
        List<ConsumerRecord<String,OrderEvent>> resultRecordList = new ArrayList<>();
        while (orderTopicIterator.hasNext()){
            ConsumerRecord<String, OrderEvent> record = orderTopicIterator.next();
            resultRecordList.add(record);
        }

        assertThat(resultRecordList.size()).isEqualTo(1);

        ConsumerRecord<String, OrderEvent> firstRecord = resultRecordList.get(0);
        OrderEvent orderEvent = firstRecord.value();
        assertThat(orderEvent.getProduct()).isEqualTo("Product");
        assertThat(orderEvent.getQuantity()).isEqualTo(11);

    }

    @Test
    @DisplayName("Testing full producer sequence")
    void fullProducerSequence(){
        Order order1 = new Order("Test product first",11);
        Order order2 = new Order("Test product second",242);
        Order order3 = new Order("Test product third",9);

        kafkaProducerService.sendOrderMessage(order1);
        kafkaProducerService.sendOrderMessage(order2);
        kafkaProducerService.sendOrderMessage(order3);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("order-consumer-group", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderEventDeserializer.class);

        KafkaConsumer<String, OrderEvent> orderEventConsumer = new KafkaConsumer<>(consumerProps);

        orderEventConsumer.subscribe(Collections.singletonList(ORDER_TOPIC_NAME));
        ConsumerRecords<String, OrderEvent> firstStepResult = orderEventConsumer.poll(Duration.of(5L, ChronoUnit.SECONDS));
        Iterable<ConsumerRecord<String, OrderEvent>> orderEventResult = firstStepResult.records(ORDER_TOPIC_NAME);
        Iterator<ConsumerRecord<String, OrderEvent>> orderEventIterator = orderEventResult.iterator();
        List<ConsumerRecord<String, OrderEvent>> orderEventRecords = new ArrayList<>();
        while (orderEventIterator.hasNext()){
            ConsumerRecord<String, OrderEvent> record = orderEventIterator.next();
            orderEventRecords.add(record);
        }

        assertThat(orderEventRecords.size()).isEqualTo(3);


        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ResponseEventSerializer.class);

        KafkaProducer<String, ResponseEvent> responseEventProducer = new KafkaProducer<>(producerProps);
        ResponseEvent event1 = new ResponseEvent("RECEIVED", Instant.now());
        ResponseEvent event2 = new ResponseEvent("RECEIVED", Instant.now());
        ResponseEvent event3 = new ResponseEvent("RECEIVED", Instant.now());
        ProducerRecord<String, ResponseEvent> producerRecord1 = new ProducerRecord<>(ORDER_STATUS_TOPIC_NAME,event1);
        ProducerRecord<String, ResponseEvent> producerRecord2 = new ProducerRecord<>(ORDER_STATUS_TOPIC_NAME,event2);
        ProducerRecord<String, ResponseEvent> producerRecord3 = new ProducerRecord<>(ORDER_STATUS_TOPIC_NAME,event3);
        responseEventProducer.send(producerRecord1);
        responseEventProducer.send(producerRecord2);
        responseEventProducer.send(producerRecord3);

        responseEventProducer.flush();
    }

    @TestConfiguration
    static class KafkaTestConfig {
        @Bean(name = "embeddedKafkaBroker")
        public EmbeddedKafkaBroker embeddedKafkaBroker() {
            return new EmbeddedKafkaZKBroker(1, true)
                    .kafkaPorts(19092);
        }
    }
}
