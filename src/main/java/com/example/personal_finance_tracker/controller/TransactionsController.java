package com.example.personal_finance_tracker.controller;

import com.example.personal_finance_tracker.dto.TransactionDTO;
import com.example.personal_finance_tracker.entity.UserEntity;
import com.example.personal_finance_tracker.service.TransactionsService;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {
    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }
    @GetMapping
    public HttpEntity<?> getTransactions(@RequestParam String month,@AuthenticationPrincipal UserEntity currentUser){
        return ResponseEntity.ok(transactionsService.getTransactionsForUser(month,currentUser));
    }

    @PostMapping
    public HttpEntity<?> saveTransactions(@ModelAttribute TransactionDTO transactionDTO,@AuthenticationPrincipal UserEntity currentUser){
      return ResponseEntity.status(HttpStatus.CREATED).body( transactionsService.saveTransaction(transactionDTO,currentUser));
    }
}
