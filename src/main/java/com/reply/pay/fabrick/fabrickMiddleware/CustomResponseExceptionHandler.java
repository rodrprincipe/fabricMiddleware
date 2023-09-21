package com.reply.pay.fabrick.fabrickMiddleware;

import com.reply.pay.fabrick.fabrickMiddleware.exception.RestServiceException;
import com.reply.pay.fabrick.fabrickMiddleware.response.upstream.UpstreamErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
@Log4j2
public class CustomResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = RestServiceException.class)
    ResponseEntity<UpstreamErrorResponse> handleDownstreamException(RestServiceException ex, HttpServletRequest request) {
        log.error("Error happened while calling {} Downstream API: {}", "<DownstreamAPI>", ex.toString());
        return new ResponseEntity<>(new UpstreamErrorResponse(ex, request.getRequestURI()), ex.getHttpStatus());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<UpstreamErrorResponse> handleException(IllegalArgumentException ex, HttpServletRequest request) {
        log.error("Exception occurred on [{}] error: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(new UpstreamErrorResponse(ex, request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    ResponseEntity<UpstreamErrorResponse> handleException(DateTimeParseException ex, RequestFacade request) {
        log.error("Exception occurred on [{}] error: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(
                new UpstreamErrorResponse(
                        new IllegalArgumentException("Date '" + ex.getParsedString() + "' not valid must have format yyyy-MM-dd"),
                        request.getRequestURI()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {

        // we can predispose to respond a list of errors, let's just reply with the first missing or invalid field
        String error = ex.getAllErrors().get(0).getDefaultMessage().toString();

        return new ResponseEntity<>(
                new UpstreamErrorResponse(
                        new IllegalArgumentException(error),
                        request.getRequestURI()
                ),
                HttpStatus.BAD_REQUEST);
    }
}