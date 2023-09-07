package com.reply.pay.fabrick.fabrickMiddleware;

import com.reply.pay.fabrick.fabrickMiddleware.exception.RestServiceException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ErrorResponse {

    private String timestamp;

    /** HTTP Status Code */
    private int status;

    /** HTTP Reason phrase */
    private String error;

    /** A message that describe the error thrown when calling the downstream API */
    private String message;

//    /** Downstream API name that has been called by this application */
//    private DownstreamApi api;

    /** URI that has been called */
    private String path;

    public ErrorResponse(RestServiceException ex, String path) {
        this.timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        this.status = ex.getStatusCode().value();
        this.error = ex.getStatusCode().getReasonPhrase();
        this.message = ex.getError();
//        this.api = ex.getApi();
        this.path = path;
    }

}