package com.roman.config;

import com.roman.service.dto.ResponseEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
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

    @Bean
    public ProducerFactory<String, ResponseEvent> responseProducerFactory(){
        return new DefaultKafkaProducerFactory<>(defaultProducerProperties());
    }

    @Bean
    public Map<String, Object> defaultProducerProperties(){
        HashMap<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:29092");
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    @Bean(name = "responseKafkaTemplate")
    public KafkaTemplate<String, ResponseEvent> responseKafkaTemplate(){
        KafkaTemplate<String, ResponseEvent> template =
                new KafkaTemplate<>(responseProducerFactory(), Collections.singletonMap(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ResponseEventSerializer.class));
        return template;
    }
}
