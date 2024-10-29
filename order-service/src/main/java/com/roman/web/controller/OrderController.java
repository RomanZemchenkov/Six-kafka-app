package com.roman.web.controller;

import com.roman.service.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/test")
    public String sendTestMessage(@RequestParam String message){
        try{
            kafkaProducer.sendTestMessage(message);
            return "Success";
        } catch (Throwable throwable){
            return "Throwable";
        }
    }
}
