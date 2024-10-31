package com.roman.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OrderEvent {

    private final String product;
    private final Integer quantity;

    @JsonCreator
    public OrderEvent(@JsonProperty(value = "product") String product,
                      @JsonProperty(value = "quantity") Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
