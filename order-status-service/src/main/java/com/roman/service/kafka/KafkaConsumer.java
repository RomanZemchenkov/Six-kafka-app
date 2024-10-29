package com.roman.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = {"testTopic"},containerFactory = "testFactory")
    public void testListener( final @Payload String message){
        System.out.println(message);
    }
}
