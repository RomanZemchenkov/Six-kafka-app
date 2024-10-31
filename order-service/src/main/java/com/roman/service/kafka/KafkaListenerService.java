package com.roman.service.kafka;

import com.roman.service.dto.ResponseEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaListenerService.class);

    @org.springframework.kafka.annotation.KafkaListener(topics = {"order-status-topic"}, containerFactory = "responseEventFactory")
    public void listenResponseEvent(ConsumerRecord<String, ResponseEvent> record){
        ResponseEvent message = record.value();
        String key = record.key();
        int partition = record.partition();
        String topic = record.topic();
        long timestamp = record.timestamp();
        logger.info("Received message: {}", message);
        logger.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);
    }

}
