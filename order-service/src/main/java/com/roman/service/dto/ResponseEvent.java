package com.roman.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
public class ResponseEvent {

    private final String status;
    private final Instant date;

    @JsonCreator
    public ResponseEvent(
            @JsonProperty(value = "status") String status,
            @JsonProperty(value = "date") Instant date) {
        this.status = status;
        this.date = date;
    }
}
