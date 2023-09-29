package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Objects;
import java.util.stream.Collectors;

public class Utilityz {
    static HttpEntity<?> buildHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<String>(headers);
    }

    static LinkedMultiValueMap<String, String> getHeaderAsMultiValueMapFrom(ResponseEntity<String> response) {
        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        response.getHeaders().toSingleValueMap().forEach(multiValueMap::add);
        return multiValueMap;
    }

    static String removeEolTo(String body) {
        return Objects.requireNonNull(body)
                .lines().collect(Collectors.joining(""));
    }

    static <T> T mapStringToClass(String string, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(
                removeEolTo(string),
                clazz);
    }

    static String json(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(o);
    }
}
