package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadStandard;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.upstream.payload.CreateMoneyTrasferPayload;
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
import java.util.Objects;

@Controller
@RequestMapping("/bankAccount")
public class MainController {
    @Value("${spring.application.name}")
    String appName;

    private final String baseUrl = "https://sandbox.platfr.io";
    private final String path = "/api/gbs/banking/v4.0/accounts";
    private final String downstreamUrl = baseUrl + path;

    private final RestTemplate restTemplate;

    private MainService mainService;

    @Autowired
    public MainController(RestTemplateBuilder builder) {
        this.restTemplate = builder.errorHandler(new CustomResponseErrorHandler())
                .defaultHeader("Auth-Schema", "S2S")
                .defaultHeader("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
                .build();

         this.mainService = new MainService();

    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<PayloadBalance> balance(@PathVariable String accountId) throws JsonProcessingException {

        String balanceUrl = downstreamUrl + "/" + accountId + "/balance";

        ResponseEntity<DownstreamSuccessfulResponsePayloadBalance> responseEntity =
                restTemplate.exchange(
                        balanceUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponsePayloadBalance.class);

        return mainService.balanceService(responseEntity);
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> transactions(@PathVariable String accountId,
                                          @RequestParam String fromAccountingDate,
                                          @RequestParam String toAccountingDate)
            throws JsonProcessingException {

        String transactionsUrl = downstreamUrl + "/" + accountId + "/" + "transactions"
                + "?" + "fromAccountingDate=" + fromAccountingDate + "&toAccountingDate=" + toAccountingDate;

        ResponseEntity<DownstreamSuccessfulResponsePayloadStandard> responseEntity =
                restTemplate.exchange(
                        transactionsUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponsePayloadStandard.class);

        return mainService.transactionService(responseEntity);
    }


    @PostMapping(
            value = "/{accountId}/payments/moneyTransfer",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> moneyTransfer(@PathVariable String accountId,
                                           @RequestBody CreateMoneyTrasferPayload createMoneyTrasferPayload) throws JsonProcessingException {
        String moneyTransferUrl = downstreamUrl + "/" + accountId + "/" + "/payments/money-transfers";

        RequestEntity<CreateMoneyTrasferPayload> requestEntity =
                RequestEntity
                        .post(URI.create(moneyTransferUrl))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createMoneyTrasferPayload);

        ResponseEntity<DownstreamSuccessfulResponsePayloadMoneyTransfer> responseEntity =
                restTemplate.exchange(
                        requestEntity,
                        DownstreamSuccessfulResponsePayloadMoneyTransfer.class);

        ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<>(
                mapper.writeValueAsString(Objects.requireNonNull(responseEntity.getBody()).getPayload()),
                responseEntity.getHeaders(),
                responseEntity.getStatusCode());

    }

}