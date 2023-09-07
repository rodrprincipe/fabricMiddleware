package com.reply.pay.fabrick.fabrickMiddleware.exception;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

// Custom runtime exception
@Getter
@Setter
public class RestServiceException extends RuntimeException {

    private String serviceName;
    private HttpStatus statusCode;
    private String error;

    public RestServiceException(
            String serviceName,
            HttpStatus statusCode,
            String error) {

        super(error);
        this.serviceName = serviceName;
        this.statusCode = statusCode;
        this.error = error;
    }

    // Error Response POJO
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestTemplateErrorResponse {
        private String status;
        private List<RestTemplateErrorElement> errors;
        private Map payload;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestTemplateErrorElement {
        private String code;
        private String description;
        private String params;
    }
}