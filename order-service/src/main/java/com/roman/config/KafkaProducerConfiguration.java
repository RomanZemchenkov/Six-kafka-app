package com.roman.config;

import com.roman.service.dto.OrderEvent;
import com.roman.service.kafka.TestKafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public ProducerFactory<String, OrderEvent> orderProducerFactory(){
        return new DefaultKafkaProducerFactory<>(defaultProducerProperties());
    }

    @Bean
    public ProducerFactory<String, String> testProducerFactory(){
        return new DefaultKafkaProducerFactory<>(defaultProducerProperties());
    }

    @Bean
    public Map<String,Object> defaultProducerProperties(){
        Map<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        return props;
    }

    @Bean(name = "testKafkaTemplate")
    public KafkaTemplate<String, String> testKafkaTemplate(){
        KafkaTemplate<String, String> template = new KafkaTemplate<>(testProducerFactory());
        template.setProducerListener(testKafkaProducer());
        return template;
    }

    @Bean(name = "orderKafkaTemplate")
    public KafkaTemplate<String, OrderEvent> orderKafkaTemplate(){
        return new KafkaTemplate<>(orderProducerFactory(), Collections.singletonMap(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderEventSerializer.class));
    }

    @Bean(value = "testKafkaProducer")
    public TestKafkaProducer testKafkaProducer(){
        return new TestKafkaProducer();
    }
}
