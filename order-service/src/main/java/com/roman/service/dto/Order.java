package com.roman.service.dto;

import lombok.Getter;

@Getter
public class Order {

    private final String product;
    private final Integer quantity;

    public Order(String product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
