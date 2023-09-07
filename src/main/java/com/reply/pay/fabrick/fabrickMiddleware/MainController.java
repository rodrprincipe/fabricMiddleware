package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Value("${spring.application.name}")
    String appName;

    String baseUrl = "https://sandbox.platfr.io";
    String path = "/api/gbs/banking/v4.0/accounts";
    String url = baseUrl + path;


    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/home")
    public String homePageUri(Model model) {
        model.addAttribute("appName", appName);
        return "homeUri";
    }

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<?> balance(@PathVariable String accountId) throws JsonProcessingException {

        String balanceUrl = url + "/" + accountId + "/balance";

        RestTemplateBuilder builder = new RestTemplateBuilder();
        String API_KEY = "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP";
        RestTemplate restTemplate = builder
                .errorHandler(new CustomResponseErrorHandler())
                .defaultHeader("Auth-Schema", "S2S")
                .defaultHeader("Api-Key", API_KEY)
                .build();

//        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("X-Time-Zone", "'Europe/Rome'");
//        headers.add("Auth-Schema", "S2S");
//        headers.add("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP");
//        headers.add("accountId","145377820");

        HttpEntity<String> request = new HttpEntity<>(headers);
//                new HttpEntity<String>(personJsonObject.toString(), headers);

//        String result = restTemplate.getForObject(balanceUrl, String.class);
//        String result = restTemplate.getForObject(balanceUrl, String.class);
//                restTemplate.postForObject(createPersonUrl, request, String.class);
//        restTemplate.setErrorHandler(new CustomResponseErrorHandler());

        ResponseEntity<String> response = restTemplate.exchange(balanceUrl, HttpMethod.GET, request, String.class);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
        String httpBodyResponse = Objects.requireNonNull(response.getBody()).lines()
                .collect(Collectors.joining(""));

//        JsonNode root = objectMapper.readTree(result);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode httpBodyResponseJson = mapper.readTree(httpBodyResponse);
        String responseBody = httpBodyResponseJson.get("payload").toString();

        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        response.getHeaders().toSingleValueMap().forEach(multiValueMap::add);
        ResponseEntity responseEntity =  new ResponseEntity<>(responseBody, multiValueMap, response.getStatusCode());

        return responseEntity;
    }

    @GetMapping("/ok")
    public ResponseEntity<?> okay() {

        return new ResponseEntity<>("okay", HttpStatus.BAD_REQUEST);
    }


//    @GetMapping("/balance2")
//    public ResponseEntity<?> balance2() {
//        String balanceUrl = "https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts";
//
//       WebClient client = WebClient.builder()
//                .baseUrl(balanceUrl)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .defaultHeader("X-Time-Zone", "'Europe/Rome'")
//                .defaultHeader("Auth-Schema", "S2S")
//                .defaultHeader("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
//                .build();
//
//        UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
//        RequestBodySpec bodySpec = uriSpec.uri("/14537780/balance");
//        return null;
//    }

//TODO error https://www.baeldung.com/spring-rest-template-error-handling
}