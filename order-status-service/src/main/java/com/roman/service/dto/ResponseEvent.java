package com.roman.service.dto;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ResponseEvent {

    private final String status;
    private final Instant date;

    public ResponseEvent(String status, Instant date) {
        this.status = status;
        this.date = date;
    }
}
