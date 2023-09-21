package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.payload.PayloadMoneyTransfer;
import com.reply.pay.fabrick.fabrickMiddleware.payload.PayloadStandard;
import com.reply.pay.fabrick.fabrickMiddleware.response.upstream.UpstreamErrorResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FabrickMiddlewareApplication.class)
class FabricMiddlewareApplicationTests {

    private static final String API_ROOT = "http://localhost:8081/bankAccount";
    private static final String ACCOUNT_ID_OK = "14537780";
    private static final String ACCOUNT_ID_KO = "9999999";

    @Test
    void contextLoads() {
    }

    @Test
    public void whenGetBalanceValidAccount_thenOK() throws JsonProcessingException {
        final Response response = RestAssured.get(API_ROOT + "/" + ACCOUNT_ID_OK + "/balance");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        PayloadBalance payloadBalance = Utilityz.mapStringToClass(response.getBody().asString(), PayloadBalance.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals(payloadBalance.getDate(), LocalDate.now().format(formatter));

        assertTrue(payloadBalance.getAvailableBalance() > 0);

    }

    @Test
    public void whenGetBalanceWithNotValidAccount_thenKO() throws JsonProcessingException {
        final Response response = RestAssured.get(API_ROOT + "/" + ACCOUNT_ID_KO + "/balance");
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());

        assertEquals("REQ004",
                Utilityz.mapStringToClass(response.getBody().asString(), UpstreamErrorResponse.class).getCode());
    }

    @Test
    public void whenGetTransactionsAccOKWithMissingTimeFrame_then400() {
        final Response response = RestAssured.get(API_ROOT + "/" + ACCOUNT_ID_OK + "/transactions");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void whenGetTransactionsAccKOWithMissingTimeFrame_then400() {
        final Response response = RestAssured.get(API_ROOT + "/" + ACCOUNT_ID_KO + "/transactions");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }


    @Test
    public void whenGetTransactionsWithNotValidAccount_then403() throws JsonProcessingException {
        final Response response = RestAssured.get(API_ROOT + "/" + ACCOUNT_ID_KO +
                "/transactions?fromAccountingDate=2019-01-28&toAccountingDate=2019-01-28");
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());

        assertEquals("REQ004",
                Utilityz.mapStringToClass(response.getBody().asString(), UpstreamErrorResponse.class).getCode());

    }

    @Test
    public void whenGetTransactionsWrongTimeFrame_then400() throws JsonProcessingException {
        final Response response = RestAssured.get(API_ROOT + "/" + ACCOUNT_ID_OK +
                "/transactions?fromAccountingDate=2019-02-28&toAccountingDate=2019-01-27");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());

        assertEquals("REQ017",
                Utilityz.mapStringToClass(response.getBody().asString(), UpstreamErrorResponse.class).getCode());
    }


    @Test
    public void whenGetTransactions_2019_01_01_thenEmptyList() throws JsonProcessingException {
        final Response response = RestAssured.get(API_ROOT + "/" + ACCOUNT_ID_OK +
                "/transactions?fromAccountingDate=2019-01-01&toAccountingDate=2019-01-01");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());


        PayloadStandard payloadGeneric = Utilityz.mapStringToClass(response.getBody().asString(), PayloadStandard.class);
        assertTrue(payloadGeneric.getList().isEmpty());
    }

    @Test
    public void whenGetTransactions2019_02_28_thenOK() throws JsonProcessingException {
        final Response response = RestAssured.get(API_ROOT + "/" + ACCOUNT_ID_KO +
                "/transactions?fromAccountingDate=2019-01-28&toAccountingDate=2019-01-28");
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());

        PayloadStandard payloadGeneric = Utilityz.mapStringToClass(response.getBody().asString(), PayloadStandard.class);
        assertFalse(payloadGeneric.getList().isEmpty());
    }

    @Test
    public void whenPostValidMoneyTransfer_thenOK() throws JsonProcessingException {
        final Response response =
                RestAssured
                        .given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(MONEY_TRASFER_BODY)
                        .post(API_ROOT + "/" + ACCOUNT_ID_OK + "payments/moneyTransfer");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        PayloadStandard payloadGeneric = Utilityz.mapStringToClass(response.getBody().asString(), PayloadStandard.class);
        assertFalse(payloadGeneric.getList().isEmpty());
    }

    @Test
    public void whenEmptyBodyMoneyTransfer_thenOK() throws JsonProcessingException {
        final Response response =
                RestAssured
                        .given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("")
                        .post(API_ROOT + "/" + ACCOUNT_ID_OK + "payments/moneyTransfer");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());

        PayloadMoneyTransfer payloadMoneyTransfer = Utilityz.mapStringToClass(response.getBody().asString(), PayloadMoneyTransfer.class);
        assertNotNull(payloadMoneyTransfer.getMoneyTransferId());
    }

    private final String MONEY_TRASFER_BODY =
            "{\n" +
                    "    \"creditor\": {\n" +
                    "        \"name\": \"John Doe\",\n" +
                    "        \"account\": {\n" +
                    "            \"accountCode\": \"IT23A0336844430152923804660\",\n" +
                    "            \"bicCode\": \"SELBIT2BXXX\"\n" +
                    "        },\n" +
                    "        \"address\": {\n" +
                    "            \"address\": null,\n" +
                    "            \"city\": null,\n" +
                    "            \"countryCode\": null\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"executionDate\": \"2019-04-01\",\n" +
                    "    \"uri\": \"REMITTANCE_INFORMATION\",\n" +
                    "    \"description\": \"Payment invoice 75/2017\",\n" +
                    "    \"amount\": 800,\n" +
                    "    \"currency\": \"EUR\",\n" +
                    "    \"isUrgent\": false,\n" +
                    "    \"isInstant\": false,\n" +
                    "    \"feeType\": \"SHA\",\n" +
                    "    \"feeAccountId\": \"14537780\",\n" +
                    "    \"taxRelief\": {\n" +
                    "        \"taxReliefId\": \"L449\",\n" +
                    "        \"isCondoUpgrade\": false,\n" +
                    "        \"creditorFiscalCode\": \"56258745832\",\n" +
                    "        \"beneficiaryType\": \"NATURAL_PERSON\",\n" +
                    "        \"naturalPersonBeneficiary\": {\n" +
                    "            \"fiscalCode1\": \"MRLFNC81L04A859L\"\n" +
                    "        },\n" +
                    "        \"legalPersonBeneficiary\": {\n" +
                    "            \"fiscalCode\": \"MRLFNC81L04A859L\",\n" +
                    "            \"legalRepresentativeFiscalCode\": null\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
}
