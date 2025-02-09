package com.bank.bankaccountmanagementsystem.controller;

import com.bank.bankaccountmanagementsystem.dto.AccountDTO;
import com.bank.bankaccountmanagementsystem.dto.AccountTransactionDTO;
import com.bank.bankaccountmanagementsystem.service.AccountService;
import com.bank.bankaccountmanagementsystem.service.AccountTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountTransactionService accountTransactionService;

    // Hata mesajı için genel metot
    private ResponseEntity<ErrorResponse> handleValidationErrors(BindingResult result) {
        StringBuilder errorMessage = new StringBuilder("Error: ");
        for (FieldError error : result.getFieldErrors()) {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage.toString()));
    }

    @Operation(summary = "Create a new bank account", description = "Creates a new account with the provided details")
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountDTO accountDTO, BindingResult result) {
        if (result.hasErrors()) {
            return handleValidationErrors(result);
        }
        try {
            AccountDTO createdAccount = accountService.createAccount(accountDTO);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Error creating account: " + e.getMessage()));
        }
    }

    @Operation(summary = "Update an existing account", description = "Updates an existing account with the provided details")
    @PutMapping("/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable UUID accountId, @RequestBody @Valid AccountDTO accountDTO, BindingResult result) {
        if (result.hasErrors()) {
            return handleValidationErrors(result);
        }
        try {
            AccountDTO updatedAccount = accountService.updateAccount(accountId, accountDTO);
            return ResponseEntity.ok(updatedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Account not found: " + e.getMessage()));
        }
    }

    @Operation(summary = "Delete an account", description = "Deletes an existing account by its ID")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID accountId) {
        try {
            accountService.deleteAccount(accountId);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Account not found: " + e.getMessage()));
        }
    }

    @Operation(summary = "Get an account by ID", description = "Retrieves account details by its ID")
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable UUID accountId) {
        try {
            AccountDTO accountDTO = accountService.getAccountDTOById(accountId);
            return ResponseEntity.ok(accountDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Account not found: " + e.getMessage()));
        }
    }

    @Operation(summary = "Deposit money into an account", description = "Deposits a specified amount into the account")
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> deposit(@PathVariable UUID accountId, @RequestParam @NotNull BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Amount must be greater than zero.").getMessage());
        }
        try {
            AccountTransactionDTO transaction = accountTransactionService.deposit(accountId, amount);
            return ResponseEntity.ok("Money deposited successfully. Transaction ID: " + transaction.getTransactionDate());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Error: " + e.getMessage()).getMessage());
        }
    }

    @Operation(summary = "Withdraw money from an account", description = "Withdraws a specified amount from the account")
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable UUID accountId, @RequestParam @NotNull BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Amount must be greater than zero.").getMessage());
        }
        try {
            AccountTransactionDTO transaction = accountTransactionService.withdraw(accountId, amount);
            return ResponseEntity.ok("Money withdrawn successfully. Transaction ID: " + transaction.getTransactionDate());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Insufficient balance or error: " + e.getMessage()).getMessage());
        }
    }
}
