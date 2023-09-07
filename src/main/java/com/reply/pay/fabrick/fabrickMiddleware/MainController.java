package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bankAccount")
public class MainController {
    @Value("${spring.application.name}")
    String appName;

    private final String API_KEY = "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP";
    private final String baseUrl = "https://sandbox.platfr.io";
    private final String path = "/api/gbs/banking/v4.0/accounts";
    private final String downstreamUrl = baseUrl + path;

    private RestTemplate restTemplate;

    @Autowired
    public MainController(RestTemplateBuilder builder) {
        this.restTemplate = builder.errorHandler(new CustomResponseErrorHandler())
                .defaultHeader("Auth-Schema", "S2S")
                .defaultHeader("Api-Key", API_KEY)
                .build();

    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> balance(@PathVariable String accountId, HttpServletRequest httpServletRequest, RequestMappingHandlerMapping mapping) throws JsonProcessingException {

        String balanceUrl = downstreamUrl + "/" + accountId + "/balance";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(balanceUrl, HttpMethod.GET, request, String.class);

        String httpBodyResponse = Objects.requireNonNull(response.getBody()).lines()
                .collect(Collectors.joining(""));

//        JsonNode root = objectMapper.readTree(result);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode httpBodyResponseJsonNode = mapper.readTree(httpBodyResponse);
        String responseBody = httpBodyResponseJsonNode.get("payload").toString();

        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        response.getHeaders().toSingleValueMap().forEach(multiValueMap::add);

        return new ResponseEntity<>(responseBody, multiValueMap, response.getStatusCode());
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> transactions(@PathVariable String accountId,
                                          @RequestParam String fromAccountingDate,
                                          @RequestParam String toAccountingDate) throws JsonProcessingException {

        String transactionsUrl = downstreamUrl + "/" + accountId + "/" + "transactions"
                + "?" + "fromAccountingDate=" + fromAccountingDate + "&toAccountingDate=" + toAccountingDate;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(transactionsUrl, HttpMethod.GET, request, String.class);

        String httpBodyResponse = Objects.requireNonNull(response.getBody()).lines()
                .collect(Collectors.joining(""));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode httpBodyResponseJsonNode = mapper.readTree(httpBodyResponse);
        String responseBody = httpBodyResponseJsonNode.get("payload").toString();

        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        response.getHeaders().toSingleValueMap().forEach(multiValueMap::add);

        return new ResponseEntity<>(responseBody, multiValueMap, response.getStatusCode());
    }

    @PostMapping("/{accountId}/payments/moneyTransfers")
    public ResponseEntity<?> transactions(@PathVariable String accountId) {
        String moneyTransferUrl = downstreamUrl + "/" + accountId + "/" + "/payments/money-transfers";
        return null;

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