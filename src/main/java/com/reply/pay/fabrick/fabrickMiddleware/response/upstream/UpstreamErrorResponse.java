package com.reply.pay.fabrick.fabrickMiddleware.response.upstream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reply.pay.fabrick.fabrickMiddleware.exception.RestServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpstreamErrorResponse {

    private String timestamp;

    /** HTTP Status Code */
    private int status;

    /** HTTP Reason phrase */
    private String error;

    /** A code that describe the error thrown when calling the downstream API */
    private String code;

    /** A message that describe the error thrown when calling the downstream API */
    private String description;

//    /** Downstream API name that has been called by this application */
//    private DownstreamApi api;

    /** URI that has been called */
    private String path;

    public UpstreamErrorResponse(RestServiceException ex, String path) {
        this.timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        this.status = ex.getHttpStatus().value();
        this.error = ex.getHttpStatus().getReasonPhrase();
        this.code = ex.getCode();
        this.description = ex.getDescription();
//        this.api = ex.getApi();
        this.path = path;
    }

    public UpstreamErrorResponse(RuntimeException ex, String path) {
        this.timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        this.status = HttpStatus.BAD_REQUEST.value();
        this.error = ex.getMessage();
        this.path = path;
    }
}