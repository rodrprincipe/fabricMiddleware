package com.reply.pay.fabrick.fabrickMiddleware;

import com.reply.pay.fabrick.fabrickMiddleware.payload.Balance;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest(classes = FabrickMiddlewareApplication.class)
public class IntegrationTest {
    private static final String ACCOUNT_ID_OK = "14537780";
    private static final String ACCOUNT_ID_KO = "9999999";

    @Mock
    private MainService mainService;

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;



    @Test
    public void test() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        when(mainService.getBalance(ACCOUNT_ID_OK)).thenReturn(new Balance());


        String url = "/bankAccount/14537780/balance";

        log.info("Testing Endpoint: " + url);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        log.info("Response: " + result.getResponse().getContentAsString());
    }
}