package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadBalance;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.upstream.payload.CreateMoneyTrasferPayload;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/bankAccount")
@Log4j2
public class MainController {
    @Value("${spring.application.name}")
    String appName;

    private final MainService mainService;

    @Autowired
    public MainController(RestTemplateBuilder builder) {
        this.mainService = new MainService(builder);
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<PayloadBalance> balance(@PathVariable String accountId) throws JsonProcessingException {
        log.info("UpStream Request [GET][balance] accountId: " + accountId);

        if (!GenericValidator.isLong(accountId)) {
            throw new IllegalArgumentException("AccountId is not valid");
        }

        return new ResponseEntity<>(
                mainService.balanceService(accountId),
                HttpStatus.OK);
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> transactions(@PathVariable String accountId,
                                          @RequestParam String fromAccountingDate,
                                          @RequestParam String toAccountingDate)
            throws JsonProcessingException {
        log.info("UpStream Request [GET][transactions] accountId: " + accountId);

        if (!GenericValidator.isLong(accountId)) {
            throw new IllegalArgumentException("AccountId is not valid");
        }

        LocalDate fromDate = LocalDate.parse(fromAccountingDate);
        LocalDate toDate = LocalDate.parse(toAccountingDate);

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("fromAccountingDate greater than toAccountingDate");
        }

        return new ResponseEntity<>(
                mainService.moneyTransferService(accountId, fromDate, toDate),
                HttpStatus.OK);
    }


    @PostMapping(
            value = "/{accountId}/payments/moneyTransfer",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> moneyTransfer(@PathVariable String accountId,
                                           @RequestBody @Valid CreateMoneyTrasferPayload createMoneyTrasferPayload)
            throws JsonProcessingException {
        log.info("UpStream Request [POST][moneyTransfer] accountId: {} payload: {}", accountId, Utilityz.json(createMoneyTrasferPayload));

        if (!GenericValidator.isLong(accountId)) {
            throw new IllegalArgumentException("AccountId is not valid");
        }

        return new ResponseEntity<>(
                mainService.moneyTransferService(accountId, createMoneyTrasferPayload),
                HttpStatus.OK);
    }
}