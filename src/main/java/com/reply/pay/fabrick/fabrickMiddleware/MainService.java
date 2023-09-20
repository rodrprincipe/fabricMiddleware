package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadStandard;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadStandard;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.upstream.payload.CreateMoneyTrasferPayload;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;

@Log4j2
public class MainService {

    private final String baseUrl = "https://sandbox.platfr.io";
    private final String path = "/api/gbs/banking/v4.0/accounts";
    private final String downstreamUrl = baseUrl + path;

    private final RestTemplate restTemplate;

    public MainService(RestTemplateBuilder builder) {
        this.restTemplate = builder.errorHandler(new CustomResponseErrorHandler())
                .defaultHeader("Auth-Schema", "S2S")
                .defaultHeader("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
                .build();
    }

    PayloadBalance balanceService(String accountId) throws JsonProcessingException {
        log.info("Entering Balance Service");
        String balanceUrl = downstreamUrl + "/" + accountId + "/balance";

        ResponseEntity<DownstreamSuccessfulResponsePayloadBalance> responseEntity =
                restTemplate.exchange(
                        balanceUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponsePayloadBalance.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));

        return responseEntity.getBody().getPayload();
    }

    PayloadStandard moneyTransferService(String accountId, LocalDate fromDate, LocalDate toDate) throws JsonProcessingException {
        log.info("Entering Transaction Service");
        String transactionsUrl = downstreamUrl + "/" + accountId + "/" + "transactions"
                + "?" + "fromAccountingDate=" + fromDate + "&toAccountingDate=" + toDate;

        ResponseEntity<DownstreamSuccessfulResponsePayloadStandard> responseEntity =
                restTemplate.exchange(
                        transactionsUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponsePayloadStandard.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));

        return responseEntity.getBody().getPayload();
    }

    PayloadMoneyTransfer moneyTransferService(String accountId, @Valid CreateMoneyTrasferPayload createMoneyTrasferPayload) throws JsonProcessingException {
        log.info("Entering Money Transfer Service");

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
        return responseEntity.getBody().getPayload();
    }
}
