package com.reply.pay.fabrick.fabrickMiddleware.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyTransfer {
    public String moneyTransferId;
    public String status;
    public String direction;
    public Creditor creditor;
    public Debtor debtor;
    public String cro;
    public String uri;
    public String trn;
    public String description;
    public Date createdDatetime;
    public Date accountedDatetime;
    public String debtorValueDate;
    public String creditorValueDate;
    public Amount amount;
    public boolean isUrgent;
    public boolean isInstant;
    public String feeType;
    public String feeAccountId;
    public ArrayList<Fee> fees;
    public boolean hasTaxRelief;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Account {
        public String accountCode;
        public String bicCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Address {
        public Object address;
        public Object city;
        public Object countryCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Amount {
        public int debtorAmount;
        public String debtorCurrency;
        public int creditorAmount;
        public String creditorCurrency;
        public String creditorCurrencyDate;
        public int exchangeRate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Creditor {
        public String name;
        public Account account;
        public Address address;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Debtor {
        public String name;
        public Account account;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Fee {
        public String feeCode;
        public String description;
        public double amount;
        public String currency;
    }
}
