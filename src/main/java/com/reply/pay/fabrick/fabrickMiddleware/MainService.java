package com.reply.pay.fabrick.fabrickMiddleware;

import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayloadStandard;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadStandard;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@Log4j2
public class MainService {
    ResponseEntity<PayloadBalance> balanceService(ResponseEntity<DownstreamSuccessfulResponsePayloadBalance> responseEntity) {
        log.info("Entering Balance Service");

        return new ResponseEntity<>(
                Objects.requireNonNull(responseEntity.getBody()).getPayload(),
                responseEntity.getHeaders(),
                responseEntity.getStatusCode());
    }

    ResponseEntity<PayloadStandard> transactionService(ResponseEntity<DownstreamSuccessfulResponsePayloadStandard> responseEntity) {
        log.info("Entering Transaction Service");

        return new ResponseEntity<>(
                Objects.requireNonNull(responseEntity.getBody()).getPayload(),
                responseEntity.getHeaders(),
                responseEntity.getStatusCode());
    }

    ResponseEntity<PayloadMoneyTransfer> moneyTransferService(ResponseEntity<DownstreamSuccessfulResponsePayloadMoneyTransfer> responseEntity) {
        log.info("Entering Money Transfer Service");

        return new ResponseEntity<>(
                Objects.requireNonNull(responseEntity.getBody()).getPayload(),
                responseEntity.getHeaders(),
                responseEntity.getStatusCode());
    }
}
