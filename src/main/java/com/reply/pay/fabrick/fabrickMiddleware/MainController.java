package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reply.pay.fabrick.fabrickMiddleware.pojoJson2csharp.CreateMoneyTransferRequest;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadMoneyTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Controller
@RequestMapping("/bankAccount")
public class MainController {
    @Value("${spring.application.name}")
    String appName;

    private final String baseUrl = "https://sandbox.platfr.io";
    private final String path = "/api/gbs/banking/v4.0/accounts";
    private final String downstreamUrl = baseUrl + path;

    private final RestTemplate restTemplate;

    @Autowired
    public MainController(RestTemplateBuilder builder) {
        this.restTemplate = builder.errorHandler(new CustomResponseErrorHandler())
                .defaultHeader("Auth-Schema", "S2S")
                .defaultHeader("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
                .build();

    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> balance(@PathVariable String accountId) throws JsonProcessingException {

        String balanceUrl = downstreamUrl + "/" + accountId + "/balance";

        ResponseEntity<String> response = restTemplate.exchange(balanceUrl, HttpMethod.GET, Utilityz.buildHttpEntity(), String.class);

        DownstreamSuccessfulResponsePayloadBalance responsePojo =
                Utilityz.mapStringToClass(response.getBody(), DownstreamSuccessfulResponsePayloadBalance.class);

        ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<>(mapper.writeValueAsString(responsePojo.getPayload()),
                Utilityz.getHeaderAsMultiValueMapFrom(response),
                response.getStatusCode());
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> transactions(@PathVariable String accountId,
                                          @RequestParam String fromAccountingDate,
                                          @RequestParam String toAccountingDate)
            throws JsonProcessingException {

        String transactionsUrl = downstreamUrl + "/" + accountId + "/" + "transactions"
                + "?" + "fromAccountingDate=" + fromAccountingDate + "&toAccountingDate=" + toAccountingDate;

        ResponseEntity<String> response = restTemplate.exchange(transactionsUrl, HttpMethod.GET, Utilityz.buildHttpEntity(), String.class);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode responseAsJsonNode = mapper.readTree(Utilityz.removeEolTo(response.getBody()));

        String responseBody = responseAsJsonNode.get("payload").toString();

        return new ResponseEntity<>(responseBody, Utilityz.getHeaderAsMultiValueMapFrom(response), response.getStatusCode());
    }


    @PostMapping(
            value = "/{accountId}/payments/moneyTransfer",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> moneyTransfer(@PathVariable String accountId,
                                           @RequestBody CreateMoneyTransferRequest createMoneyTransferRequest) {
        String moneyTransferUrl = downstreamUrl + "/" + accountId + "/" + "/payments/money-transfers";

        RequestEntity<CreateMoneyTransferRequest> requestEntity =
                RequestEntity
                        .post(URI.create(moneyTransferUrl))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createMoneyTransferRequest);

        ResponseEntity<DownstreamSuccessfulResponsePayloadMoneyTransfer> response =
                restTemplate.exchange(
                        requestEntity,
                        DownstreamSuccessfulResponsePayloadMoneyTransfer.class);

        return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());

    }

}