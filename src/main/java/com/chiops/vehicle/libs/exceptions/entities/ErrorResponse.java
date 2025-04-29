package com.chiops.vehicle.libs.exceptions.entities;

import io.micronaut.http.HttpStatus;
import io.micronaut.serde.annotation.Serdeable.Serializable;

import java.time.Instant;

@Serializable
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;


    public ErrorResponse(HttpStatus status, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status.getCode();
        this.error = status.name();
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

}