package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayloadBalance {
    private String date;
    private double balance;
    private double availableBalance;
    private String currency;
}
