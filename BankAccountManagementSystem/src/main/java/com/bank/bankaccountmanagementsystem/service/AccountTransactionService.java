package com.bank.bankaccountmanagementsystem.service;

import com.bank.bankaccountmanagementsystem.dto.AccountTransactionDTO;
import com.bank.bankaccountmanagementsystem.model.Account;
import com.bank.bankaccountmanagementsystem.model.AccountTransaction;
import com.bank.bankaccountmanagementsystem.repository.AccountRepository;
import com.bank.bankaccountmanagementsystem.repository.AccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccountTransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;


    private AccountTransactionDTO processTransaction(UUID accountId, BigDecimal amount, boolean isDeposit) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        BigDecimal newBalance = isDeposit ? account.getBalance().add(amount) : account.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot fall below zero.");
        }
        if (newBalance.compareTo(new BigDecimal(9999999)) > 0) {
            throw new IllegalArgumentException("Balance exceeds the limit.");
        }

        account.setBalance(newBalance);
        accountRepository.save(account);

        AccountTransaction transaction = new AccountTransaction();
        transaction.setAccount(account);
        transaction.setTransactionType(isDeposit ? AccountTransaction.TransactionType.DEPOSIT : AccountTransaction.TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        accountTransactionRepository.save(transaction);

        // Return transaction as DTO
        return new AccountTransactionDTO(
                account.getId().toString(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getTransactionDate()
        );
    }


    public AccountTransactionDTO deposit(UUID accountId, BigDecimal amount) {
        return processTransaction(accountId, amount, true);
    }


    public AccountTransactionDTO withdraw(UUID accountId, BigDecimal amount) {
        return processTransaction(accountId, amount, false);
    }
}