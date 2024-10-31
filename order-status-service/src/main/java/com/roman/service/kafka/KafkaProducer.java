package com.roman.service.kafka;

import com.roman.service.dto.ResponseEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, ResponseEvent> responseTemplate;
    private final String orderStatusTopic = "order-status-topic";

    @Autowired
    public KafkaProducer(
            @Qualifier(value = "responseKafkaTemplate") KafkaTemplate<String, ResponseEvent> responseTemplate
    ) {
        this.responseTemplate = responseTemplate;
    }

    public void sendResponse(ResponseEvent responseEvent){
        ProducerRecord<String, ResponseEvent> record = new ProducerRecord<>(orderStatusTopic, responseEvent);
        responseTemplate.send(record);
    }
}
