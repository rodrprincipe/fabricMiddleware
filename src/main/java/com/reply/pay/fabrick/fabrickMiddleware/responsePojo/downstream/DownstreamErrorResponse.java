package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownstreamErrorResponse {
    private String status;
    private List<ErrorElement> errors;
    private Map payload;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorElement {
        private String code;
        private String description;
        private String params;
    }
}
