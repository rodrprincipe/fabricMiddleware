package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomResponseErrorHandler
        implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (httpResponse.getStatusCode().is4xxClientError()
                || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse response)
            throws IOException {

//        if (httpResponse.getStatusCode().is5xxServerError()) {
//            // handle SERVER_ERROR
//            throw new HttpClientErrorException(HttpStatus.OK);
//
//        } else
//        if (httpResponse.getStatusCode().is4xxClientError()) {
//
//            // handle CLIENT_ERROR
//            throw new HttpClientErrorException(HttpStatus.OK);
//        }
//        if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
//            throw new NotFoundException();
//        }

        if (response.getStatusCode().is4xxClientError()
                || response.getStatusCode().is5xxServerError()) {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {

                String httpBodyResponse = reader.lines()
                        .collect(Collectors.joining(""));

                ObjectMapper mapper = new ObjectMapper();

                RestTemplateResponseError restTemplateError =
                        mapper.readValue(
                                httpBodyResponse,
                                RestTemplateResponseError.class);

                throw new RestServiceException(
                        "host",
                        HttpStatus.valueOf(response.getStatusCode().value()),
                        restTemplateError.getErrors().get(0).description);
            }

        }
    }

    // Custom runtime exception
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
    }

    // Error Response POJO
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestTemplateResponseError {
        private String status;
        private String error;
        private List<RestTemplateErrorElement> errors;
        private Object payload;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestTemplateErrorElement {
        private String code;
        private String description;
        private String params;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestTemplatePayloadElement {
        private String code;
        private String description;
        private String params;
    }
}