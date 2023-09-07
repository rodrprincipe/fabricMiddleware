package com.reply.pay.fabrick.fabrickMiddleware;

import com.reply.pay.fabrick.fabrickMiddleware.exception.RestServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomResponseExceptionHandler.class);

    @ExceptionHandler(value = RestServiceException.class)
    ResponseEntity<ErrorResponse> handleMyRestTemplateException(RestServiceException ex, HttpServletRequest request) {
        LOGGER.error("An error happened while calling {} Downstream API: {}", "<PATH>", ex.toString());
        return new ResponseEntity<>(new ErrorResponse(ex, request.getRequestURI()), ex.getStatusCode());
    }
}