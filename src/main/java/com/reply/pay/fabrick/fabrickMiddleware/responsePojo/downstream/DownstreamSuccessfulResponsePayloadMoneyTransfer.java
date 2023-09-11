package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream;

import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadMoneyTransfer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class DownstreamSuccessfulResponsePayloadMoneyTransfer extends DownstreamSuccessfulResponse {
    private PayloadMoneyTransfer payload;

}