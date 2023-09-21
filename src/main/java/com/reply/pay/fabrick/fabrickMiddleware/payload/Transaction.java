package com.reply.pay.fabrick.fabrickMiddleware.payload;

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
public class Transaction {

    public String transactionId;
    public String operationId;
    public String accountingDate;
    public String valueDate;
    public Type type;
    public int amount;
    public String currency;
    public String description;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Type {
        public String enumeration;
        public String value;
    }
}