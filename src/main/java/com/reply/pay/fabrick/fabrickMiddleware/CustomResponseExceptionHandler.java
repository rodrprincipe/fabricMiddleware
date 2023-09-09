package com.reply.pay.fabrick.fabrickMiddleware;

import com.reply.pay.fabrick.fabrickMiddleware.exception.RestServiceException;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.upstream.UpstreamErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomResponseExceptionHandler.class);

    @ExceptionHandler(value = RestServiceException.class)
    ResponseEntity<UpstreamErrorResponse> handleRestServiceException(RestServiceException ex, HttpServletRequest request) {
        LOGGER.error("An error happened while calling {} Downstream API: {}", "<DownstreamAPI>", ex.toString());
        return new ResponseEntity<>(new UpstreamErrorResponse(ex, request.getRequestURI()), ex.getHttpStatus());
    }
}