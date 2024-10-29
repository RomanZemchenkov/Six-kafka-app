package com.roman.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {


    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String, Object> conf = new HashMap<>();

        conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:29092");
        KafkaAdmin kafkaAdmin = new KafkaAdmin(conf);
        kafkaAdmin.createOrModifyTopics(newTestTopic());

        return new KafkaAdmin(conf);
    }

    @Bean(name = "testTopic")
    public NewTopic newTestTopic(){
        NewTopic topic = TopicBuilder.name("testTopic")
                .partitions(1)
                .replicas(1)
                .build();
        return topic;
    }
}
