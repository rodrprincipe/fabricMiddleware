package com.reply.pay.fabrick.fabrickMiddleware;

import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.*;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadStandard;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class MainService {
    ResponseEntity<PayloadBalance> balanceService(ResponseEntity<DownstreamSuccessfulResponsePayloadBalance> responseEntity) {

        return new ResponseEntity<>(
                Objects.requireNonNull(responseEntity.getBody()).getPayload(),
                responseEntity.getHeaders(),
                responseEntity.getStatusCode());
    }

    ResponseEntity<PayloadStandard> transactionService(ResponseEntity<DownstreamSuccessfulResponsePayloadStandard> responseEntity) {

        return new ResponseEntity<>(
                Objects.requireNonNull(responseEntity.getBody()).getPayload(),
                responseEntity.getHeaders(),
                responseEntity.getStatusCode());
    }

    ResponseEntity<PayloadMoneyTransfer> moneyTransferService(ResponseEntity<DownstreamSuccessfulResponsePayloadMoneyTransfer> responseEntity) {

        return new ResponseEntity<>(
                Objects.requireNonNull(responseEntity.getBody()).getPayload(),
                responseEntity.getHeaders(),
                responseEntity.getStatusCode());
    }
}
