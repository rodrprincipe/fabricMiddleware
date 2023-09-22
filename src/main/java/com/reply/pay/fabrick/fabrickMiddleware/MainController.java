package com.reply.pay.fabrick.fabrickMiddleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reply.pay.fabrick.fabrickMiddleware.payload.Balance;
import com.reply.pay.fabrick.fabrickMiddleware.payload.CreateMoneyTrasfer;
import com.reply.pay.fabrick.fabrickMiddleware.payload.Transaction;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Log4j2
@Controller
@RequestMapping("/bankAccount")
public class MainController {
    @Value("${spring.application.name}")
    String appName;

    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Balance> balance(@PathVariable String accountId) throws JsonProcessingException {
        log.info("UpStream Request [GET][balance] accountId: " + accountId);

        if (!GenericValidator.isLong(accountId)) {
            throw new IllegalArgumentException("AccountId is not valid");
        }

        return new ResponseEntity<>(
                mainService.getBalance(accountId),
                HttpStatus.OK);
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<ArrayList<Transaction>> transactions(@PathVariable String accountId,
                                                               @RequestParam String fromAccountingDate,
                                                               @RequestParam String toAccountingDate)
            throws JsonProcessingException {
        log.info("UpStream Request [GET][transactions] accountId: {} | from: {} | to: {} ", accountId, fromAccountingDate, toAccountingDate);

        if (!GenericValidator.isLong(accountId)) {
            throw new IllegalArgumentException("AccountId is not valid");
        }

        LocalDate fromDate = LocalDate.parse(fromAccountingDate);
        LocalDate toDate = LocalDate.parse(toAccountingDate);

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("fromAccountingDate greater than toAccountingDate");
        }

        return new ResponseEntity<>(
                mainService.getTransactions(accountId, fromDate, toDate),
                HttpStatus.OK);
    }


    @PostMapping(
            value = "/{accountId}/payments/moneyTransfer",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> moneyTransfer(@PathVariable String accountId,
                                           @RequestBody @Valid CreateMoneyTrasfer createMoneyTrasferPayload)
            throws JsonProcessingException {
        log.info("UpStream Request [POST][moneyTransfer] accountId: {} payload: {}", accountId, Utilityz.json(createMoneyTrasferPayload));

        if (!GenericValidator.isLong(accountId)) {
            throw new IllegalArgumentException("AccountId is not valid");
        }

        return new ResponseEntity<>(
                mainService.performMoneyTransfer(accountId, createMoneyTrasferPayload),
                HttpStatus.OK);
    }
}