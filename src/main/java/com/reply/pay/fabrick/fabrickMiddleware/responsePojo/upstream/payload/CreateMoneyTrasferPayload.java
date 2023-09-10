package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.upstream.payload;

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
public class CreateMoneyTrasferPayload {
    public Creditor creditor;
    public String executionDate;
    public String uri;
    public String description;
    public int amount;
    public String currency;
    public boolean isUrgent;
    public boolean isInstant;
    public String feeType;
    public String feeAccountId;
    public TaxRelief taxRelief;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        public String accountCode;
        public String bicCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        public Object address;
        public Object city;
        public Object countryCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Creditor {
        public String name;
        public Account account;
        public Address address;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LegalPersonBeneficiary {
        public Object fiscalCode;
        public Object legalRepresentativeFiscalCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaturalPersonBeneficiary {
        public String fiscalCode1;
        public Object fiscalCode2;
        public Object fiscalCode3;
        public Object fiscalCode4;
        public Object fiscalCode5;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxRelief {
        public String taxReliefId;
        public boolean isCondoUpgrade;
        public String creditorFiscalCode;
        public String beneficiaryType;
        public NaturalPersonBeneficiary naturalPersonBeneficiary;
        public LegalPersonBeneficiary legalPersonBeneficiary;
    }
}
