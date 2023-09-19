package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadStandard;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.upstream.payload.CreateMoneyTrasferPayload;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class MainController {
    @Value("${spring.application.name}")
    String appName;

    private final String baseUrl = "https://sandbox.platfr.io";
    private final String path = "/api/gbs/banking/v4.0/accounts";
    private final String downstreamUrl = baseUrl + path;

    private final RestTemplate restTemplate;

    private final MainService mainService;

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
        log.info("UpStream Request [GET][balance] accountId: " + accountId);

        String balanceUrl = downstreamUrl + "/" + accountId + "/balance";

        ResponseEntity<DownstreamSuccessfulResponsePayloadBalance> responseEntity =
                restTemplate.exchange(
                        balanceUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponsePayloadBalance.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));

        return mainService.balanceService(responseEntity);
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> transactions(@PathVariable String accountId,
                                          @RequestParam String fromAccountingDate,
                                          @RequestParam String toAccountingDate)
            throws JsonProcessingException {
        log.info("UpStream Request [GET][transactions] accountId: " + accountId);


        String transactionsUrl = downstreamUrl + "/" + accountId + "/" + "transactions"
                + "?" + "fromAccountingDate=" + fromAccountingDate + "&toAccountingDate=" + toAccountingDate;

        ResponseEntity<DownstreamSuccessfulResponsePayloadStandard> responseEntity =
                restTemplate.exchange(
                        transactionsUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponsePayloadStandard.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));

        return mainService.transactionService(responseEntity);
    }


    @PostMapping(
            value = "/{accountId}/payments/moneyTransfer",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> moneyTransfer(@PathVariable String accountId,
                                           @RequestBody CreateMoneyTrasferPayload createMoneyTrasferPayload)
            throws JsonProcessingException {
        log.info("UpStream Request [POST][moneyTransfer] accountId: " + accountId);

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

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));

        return mainService.moneyTransferService(responseEntity);

    }

}