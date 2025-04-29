package com.chiops.vehicle.libs.exceptions.exception;

public class ParseError extends BadRequestException {
    public ParseError(String message) {
        super(message);
    }
}