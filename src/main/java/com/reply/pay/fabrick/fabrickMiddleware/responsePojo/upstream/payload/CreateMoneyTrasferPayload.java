package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.upstream.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMoneyTrasferPayload {
    @Valid
    @NotNull(message = "creditor required")
    public Creditor creditor;
    @NotNull(message = "executionDate required")
    public LocalDate executionDate;
    public String uri;
    @NotNull(message = "description required")
    public String description;
    @NotNull(message = "amount required")
    @Min(value = 1, message = "amount must be greater than 1")
    public float amount;
    @NotNull(message = "currency required")
    public String currency;
    public boolean isUrgent;
    public boolean isInstant;
    public String feeType;
    public String feeAccountId;
    @Valid
    @NotNull(message = "taxRelief required")
    public TaxRelief taxRelief;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        @Valid
        @NotNull(message = "creditor.account.accountCode required")
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
        @NotNull(message = "creditor.name required")
        public String name;
        @Valid
        @NotNull(message = "creditor.account required")
        public Account account;
        public Address address;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LegalPersonBeneficiary {
        @NotNull(message = "taxRelief.beneficiaryType.legalPersonBeneficiary.fiscalCode required")
        public String fiscalCode;
        public String legalRepresentativeFiscalCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaturalPersonBeneficiary {
        @NotNull(message = "taxRelief.beneficiaryType.naturalPersonBeneficiary.fiscalCode1 required")
        public String fiscalCode1;
        public String fiscalCode2;
        public String fiscalCode3;
        public String fiscalCode4;
        public String fiscalCode5;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxRelief {
        public String taxReliefId;
        @NotNull(message = "taxRelief.isCondoUpgrade required")
        public boolean isCondoUpgrade;
        @NotNull(message = "taxRelief.creditorFiscalCode required")
        public String creditorFiscalCode;
        @NotNull(message = "taxRelief.beneficiaryType required")
        public String beneficiaryType;
        @NotNull(message = "taxRelief.beneficiaryType.naturalPersonBeneficiary required")
        public NaturalPersonBeneficiary naturalPersonBeneficiary;
        @NotNull(message = "taxRelief.beneficiaryType.legalPersonBeneficiary required")
        public LegalPersonBeneficiary legalPersonBeneficiary;
    }
}
