package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.payload.Balance;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest
@RunWith(SpringRunner.class)
public class MainControllerMockMvcTest {
    private static final String ACCOUNT_ID_OK = "14537780";
    private static final String ACCOUNT_ID_KO = "9999999";

    @Mock
    private MainService mainService;

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;


    @Before
    public void setUp() throws JsonProcessingException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();


    }

    @Test
    public void whenGetBalanceValidAccount_thenOK() throws Exception {
        String url = "/bankAccount/" + ACCOUNT_ID_OK + "/balance";
        when(mainService.getBalance(ACCOUNT_ID_OK)).thenReturn(new Balance());

        log.info("Testing Endpoint: " + url);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").isNumber())
                .andReturn();

        log.info("Response: " + result.getResponse().getContentAsString());
    }

    @Test
    public void whenGetBalanceNotValidAccount_thenKO() throws Exception {
        String url = "/bankAccount/" + ACCOUNT_ID_KO + "/balance";
        when(mainService.getBalance(ACCOUNT_ID_KO)).thenThrow();


        log.info("Testing Endpoint: " + url);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().is4xxClientError())
                .andReturn();

        log.info("Response: " + result.getResponse().getContentAsString());
    }
}