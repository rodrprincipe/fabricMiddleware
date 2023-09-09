package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadGeneric;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadMoneyTransfer;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @Type(value = PayloadBalance.class, name = "balance"),
        @Type(value = PayloadMoneyTransfer.class, name = "moneyTransfer"),
        @Type(value = PayloadGeneric.class, name = "generic")
})
public abstract class DownstreamSuccessfulResponsePayload {
    public DownstreamSuccessfulResponsePayload() {
    }
}