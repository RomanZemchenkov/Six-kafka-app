package com.roman.service.kafka;

import com.roman.service.dto.Order;
import com.roman.service.dto.OrderEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, OrderEvent> orderKafkaTemplate;

    private final String orderTopicName = "order-topic";

    @Autowired
    public KafkaProducerService(
                                @Qualifier(value = "orderKafkaTemplate") KafkaTemplate<String, OrderEvent> orderKafkaTemplate) {
        this.orderKafkaTemplate = orderKafkaTemplate;
    }

    public void sendOrderMessage(Order order){
        OrderEvent event = new OrderEvent(order.getProduct(), order.getQuantity());
        ProducerRecord<String, OrderEvent> record = new ProducerRecord<>(orderTopicName, event);
        orderKafkaTemplate.send(record);

        orderKafkaTemplate.setProducerListener(new ProducerListener<>() {
            @Override
            public void onSuccess(ProducerRecord<String, OrderEvent> producerRecord, RecordMetadata recordMetadata) {
                OrderEvent message = producerRecord.value();
                System.out.printf("Сообщение: '%s %d' успешно доставлено.\n", message.getProduct(),message.getQuantity());
            }
        });
    }

}
