package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream;

//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY)
//@JsonSubTypes({
//        @Type(value = PayloadBalance.class, name = "balance"),
//        @Type(value = PayloadMoneyTransfer.class, name = "moneyTransfer"),
//        @Type(value = PayloadGeneric.class, name = "generic")
//})
public abstract class DownstreamSuccessfulResponsePayload {
    public DownstreamSuccessfulResponsePayload() {
    }
}