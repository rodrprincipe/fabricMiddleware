package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.payload.Balance;
import com.reply.pay.fabrick.fabrickMiddleware.payload.CreateMoneyTrasfer;
import com.reply.pay.fabrick.fabrickMiddleware.payload.MoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.payload.Transaction;
import com.reply.pay.fabrick.fabrickMiddleware.response.downstream.DownstreamSuccessfulResponseBalance;
import com.reply.pay.fabrick.fabrickMiddleware.response.downstream.DownstreamSuccessfulResponseMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.response.downstream.DownstreamSuccessfulResponseTransactions;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

@Log4j2
@Service
public class MainService {

    private final RestTemplate restTemplate;

    @Autowired
    public MainService(@Value("${service.baseUrl}") String baseUrl, RestTemplateBuilder builder) {
        log.info(builder);
        this.restTemplate = builder.rootUri(baseUrl)
                .errorHandler(new CustomResponseErrorHandler())
                .defaultHeader("Auth-Schema", "S2S")
                .defaultHeader("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
                .build();
    }

    Balance getBalance(String accountId) throws JsonProcessingException {
        log.info("Entering Balance Service");
        String balanceUrl = "/" + accountId + "/balance";

        ResponseEntity<DownstreamSuccessfulResponseBalance> responseEntity =
                restTemplate.exchange(
                        balanceUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponseBalance.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));

        return Objects.requireNonNull(responseEntity.getBody()).getPayload();
    }

    ArrayList<Transaction> getTransactions(String accountId, LocalDate fromDate, LocalDate toDate) throws JsonProcessingException {
        log.info("Entering Transaction Service");
        String transactionsUrl = "/" + accountId + "/" + "transactions"
                + "?" + "fromAccountingDate=" + fromDate + "&toAccountingDate=" + toDate;

        ResponseEntity<DownstreamSuccessfulResponseTransactions> responseEntity =
                restTemplate.exchange(
                        transactionsUrl,
                        HttpMethod.GET,
                        Utilityz.buildHttpEntity(),
                        DownstreamSuccessfulResponseTransactions.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));

        return Objects.requireNonNull(responseEntity.getBody()).getPayload().getList();
    }

    MoneyTransfer performMoneyTransfer(String accountId, @Valid CreateMoneyTrasfer createMoneyTransferPayload) throws JsonProcessingException {
        log.info("Entering Money Transfer Service");

        String moneyTransferUrl = "/" + accountId + "/payments/money-transfers";

        RequestEntity<CreateMoneyTrasfer> requestEntity =
                RequestEntity
                        .post(moneyTransferUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createMoneyTransferPayload);

        ResponseEntity<DownstreamSuccessfulResponseMoneyTransfer> responseEntity =
                restTemplate.exchange(
                        requestEntity,
                        DownstreamSuccessfulResponseMoneyTransfer.class);

        log.info(String.format("DownStream Response [%s] %s", responseEntity.getStatusCode(), Utilityz.json(responseEntity.getBody())));
        return Objects.requireNonNull(responseEntity.getBody()).getPayload();
    }
}
