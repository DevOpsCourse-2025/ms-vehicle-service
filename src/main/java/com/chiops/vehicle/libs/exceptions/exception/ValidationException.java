package com.chiops.vehicle.libs.exceptions.exception;

public class ValidationException extends BadRequestException {
    public ValidationException(String message) {
        super(message);
    }
}
