package com.roman.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAdminConfiguration {

    @Value("${spring.kafka.admin.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String, Object> conf = new HashMap<>();

        conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        KafkaAdmin kafkaAdmin = new KafkaAdmin(conf);
        kafkaAdmin.createOrModifyTopics(orderTopic());
        kafkaAdmin.createOrModifyTopics(orderStatusTopic());

        return new KafkaAdmin(conf);
    }

    @Bean(name = "orderTopic")
    public NewTopic orderTopic(){
        return TopicBuilder.name("order-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean(name = "orderStatusTopic")
    public NewTopic orderStatusTopic(){
        return TopicBuilder.name("order-status-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
