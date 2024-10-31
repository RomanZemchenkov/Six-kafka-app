package com.roman.web.controller;

import com.roman.service.dto.Order;
import com.roman.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final KafkaProducerService kafkaProducerService;


    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Order order){
        kafkaProducerService.sendOrderMessage(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
