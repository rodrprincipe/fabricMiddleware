package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reply.pay.fabrick.fabrickMiddleware.exception.NotFoundException;
import com.reply.pay.fabrick.fabrickMiddleware.exception.RestServiceException;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (httpResponse.getStatusCode().is4xxClientError()
                || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse response)
            throws IOException {

        if (response.getStatusCode().is4xxClientError()
                || response.getStatusCode().is5xxServerError()) {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {

                String httpBodyResponse = reader.lines()
                        .collect(Collectors.joining(""));

                ObjectMapper mapper = new ObjectMapper();

                DownstreamErrorResponse restTemplateErrorResponse =
                        mapper.readValue(
                                httpBodyResponse,
                                DownstreamErrorResponse.class);

                String code = restTemplateErrorResponse.getErrors().get(0).getCode();
                String description = restTemplateErrorResponse.getErrors().get(0).getDescription();

                if (Objects.equals(code, "REQ004")) {
                    throw new NotFoundException(
                            "<DownstreamAPI>",
                            HttpStatus.valueOf(response.getStatusCode().value()),
                            code,
                            description);
                } else {
                    throw new RestServiceException(
                            "<DownstreamAPI>",
                            HttpStatus.valueOf(response.getStatusCode().value()),
                            code,
                            description);
                }
            }

        }
    }

}