package com.bank.bankaccountmanagementsystem.service;

import com.bank.bankaccountmanagementsystem.dto.AccountTransactionDTO;
import com.bank.bankaccountmanagementsystem.model.Account;
import com.bank.bankaccountmanagementsystem.model.AccountTransaction;
import com.bank.bankaccountmanagementsystem.repository.AccountRepository;
import com.bank.bankaccountmanagementsystem.repository.AccountTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountTransactionRepository accountTransactionRepository;

    @InjectMocks
    private AccountTransactionService accountTransactionService;

    private Account account;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountId = UUID.randomUUID();
        account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(5000));
    }

    @Test
    void deposit_shouldIncreaseBalance() {

        BigDecimal depositAmount = BigDecimal.valueOf(1000);


        BigDecimal expectedBalance = account.getBalance().add(depositAmount);

        AccountTransactionDTO expectedTransactionDTO = new AccountTransactionDTO(
                accountId.toString(),
                AccountTransaction.TransactionType.DEPOSIT,
                depositAmount,
                LocalDateTime.now()
        );

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));


        Mockito.when(accountTransactionRepository.save(Mockito.any(AccountTransaction.class)))
                .thenReturn(new AccountTransaction());


        AccountTransactionDTO transactionDTO = accountTransactionService.deposit(accountId, depositAmount);


        assertNotNull(transactionDTO);
        assertEquals(expectedBalance, account.getBalance());
        assertEquals(AccountTransaction.TransactionType.DEPOSIT, transactionDTO.getTransactionType());
    }

    @Test
    void withdraw_shouldDecreaseBalance() {

        BigDecimal withdrawalAmount = BigDecimal.valueOf(1000);
        AccountTransactionDTO expectedTransactionDTO = new AccountTransactionDTO(
                accountId.toString(),
                AccountTransaction.TransactionType.WITHDRAWAL,
                withdrawalAmount,
                LocalDateTime.now()
        );

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));


        Mockito.when(accountTransactionRepository.save(Mockito.any(AccountTransaction.class)))
                .thenReturn(new AccountTransaction());


        AccountTransactionDTO transactionDTO = accountTransactionService.withdraw(accountId, withdrawalAmount);


        assertNotNull(transactionDTO);
        assertEquals(BigDecimal.valueOf(4000), account.getBalance());
        assertEquals(AccountTransaction.TransactionType.WITHDRAWAL, transactionDTO.getTransactionType());
    }

    @Test
    void withdraw_shouldThrowExceptionIfBalanceIsLow() {

        BigDecimal withdrawalAmount = BigDecimal.valueOf(6000);

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountTransactionService.withdraw(accountId, withdrawalAmount);
        });
        assertEquals("Balance cannot fall below zero.", exception.getMessage());
    }

    @Test
    void deposit_shouldThrowExceptionIfNewBalanceExceedsLimit() {

        BigDecimal depositAmount = BigDecimal.valueOf(10000000);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountTransactionService.deposit(accountId, depositAmount);
        });
        assertEquals("Balance exceeds the limit.", exception.getMessage());
    }

    @Test
    void processTransaction_shouldThrowExceptionIfAccountNotFound() {

        BigDecimal amount = BigDecimal.valueOf(1000);

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountTransactionService.deposit(accountId, amount);
        });
        assertEquals("Account not found", exception.getMessage());
    }
}