package com.roman.service.kafka;

import com.roman.service.dto.OrderEvent;
import com.roman.service.dto.ResponseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class KafkaListenerService {

    private final KafkaProducer kafkaProducer;

    @Autowired
    public KafkaListenerService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @KafkaListener(topics = {"order-topic"}, containerFactory = "orderFactory")
    public void orderListener(final @Payload OrderEvent order){
        ResponseEvent event = new ResponseEvent("RECEIVED", Instant.now());
        kafkaProducer.sendResponse(event);
    }
}
