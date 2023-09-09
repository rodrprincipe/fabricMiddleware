package com.reply.pay.fabrick.fabrickMiddleware.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

// Custom runtime exception
@Getter
@Setter
public class RestServiceException extends RuntimeException {

    private String serviceName;
    private HttpStatus httpStatus;
    private String code;
    private String description;



    public RestServiceException(
            String serviceName,
            HttpStatus httpStatus,
            String code,
            String description) {

        super(code + " - " + description);
        this.serviceName = serviceName;
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
    }
}