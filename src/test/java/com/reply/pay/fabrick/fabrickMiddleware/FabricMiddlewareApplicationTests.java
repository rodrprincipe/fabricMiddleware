package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadGeneric;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.upstream.UpstreamErrorResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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


        PayloadGeneric payloadGeneric = Utilityz.mapStringToClass(response.getBody().asString(), PayloadGeneric.class);
        assertTrue(payloadGeneric.getList().isEmpty());
    }

    @Test
    public void whenGetTransactions2019_02_28_thenOK() throws JsonProcessingException {
        final Response response = RestAssured.get(API_ROOT + "/" + ACCOUNT_ID_KO +
                "/transactions?fromAccountingDate=2019-01-28&toAccountingDate=2019-01-28");
        assertEquals(HttpStatus.valueOf(403).value(), response.getStatusCode());

        PayloadGeneric payloadGeneric = Utilityz.mapStringToClass(response.getBody().asString(), PayloadGeneric.class);
        assertFalse(payloadGeneric.getList().isEmpty());
    }


}
