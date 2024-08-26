package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.payload.Balance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MainControllerMockitoTest {
    private static final String ACCOUNT_ID_OK = "14537780";

    @Mock
    private MainService mainService;

    @InjectMocks
    private MainController mainController;

    @Test
    public void balanceReturnGreaterThenZero() throws JsonProcessingException {
        Balance balance = getJsonResource("/jsonSamples/balance.json", Balance.class);

        Mockito.when(mainService.getBalance(ACCOUNT_ID_OK)).
                thenReturn(balance);

        assertTrue(Objects.requireNonNull(mainController.balance(ACCOUNT_ID_OK).getBody()).getBalance()>0);
    }


    <T> T getJsonResource(String resourcePath, Class<T> clazz) throws JsonProcessingException {
        String resourceAsString =
                new BufferedReader(
                        new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(resourcePath))))
                        .lines().collect(Collectors.joining(""));
        return Utilityz.mapStringToClass(resourceAsString, clazz);
    }
}
