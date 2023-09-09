package com.reply.pay.fabrick.fabrickMiddleware.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestServiceException {
    public NotFoundException(String serviceName, HttpStatus httpStatus, String code, String description) {
        super(serviceName, httpStatus, code, description);
    }
}