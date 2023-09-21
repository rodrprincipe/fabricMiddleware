package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.payload.Transaction;
import com.reply.pay.fabrick.fabrickMiddleware.response.downstream.*;
import com.reply.pay.fabrick.fabrickMiddleware.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.payload.PayloadMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.payload.PayloadStandard;
import com.reply.pay.fabrick.fabrickMiddleware.payload.PayloadCreateMoneyTrasfer;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;

@Log4j2
@Service
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

        ResponseEntity<DownstreamSuccessfulResponseBalance> responseEntity =
                restTemplate.exchange(
                        balanceUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponseBalance.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));

        return Objects.requireNonNull(responseEntity.getBody()).getPayload();
    }

    PayloadStandard<Transaction> transactionsService(String accountId, LocalDate fromDate, LocalDate toDate) throws JsonProcessingException {
        log.info("Entering Transaction Service");
        String transactionsUrl = downstreamUrl + "/" + accountId + "/" + "transactions"
                + "?" + "fromAccountingDate=" + fromDate + "&toAccountingDate=" + toDate;

        ResponseEntity<DownstreamSuccessfulResponseTransactions> responseEntity =
                restTemplate.exchange(
                        transactionsUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponseTransactions.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));

        return Objects.requireNonNull(responseEntity.getBody()).getPayload();
    }

    PayloadMoneyTransfer moneyTransferService(String accountId, @Valid PayloadCreateMoneyTrasfer createMoneyTrasferPayload) throws JsonProcessingException {
        log.info("Entering Money Transfer Service");

        String moneyTransferUrl = downstreamUrl + "/" + accountId + "/" + "/payments/money-transfers";

        RequestEntity<PayloadCreateMoneyTrasfer> requestEntity =
                RequestEntity
                        .post(URI.create(moneyTransferUrl))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createMoneyTrasferPayload);

        ResponseEntity<DownstreamSuccessfulResponseMoneyTransfer> responseEntity =
                restTemplate.exchange(
                        requestEntity,
                        DownstreamSuccessfulResponseMoneyTransfer.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));
        return Objects.requireNonNull(responseEntity.getBody()).getPayload();
    }
}
