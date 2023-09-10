package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayloadBalance extends DownstreamSuccessfulResponsePayload {
    private String date;
    private double balance;
    private double availableBalance;
    private String currency;
}
