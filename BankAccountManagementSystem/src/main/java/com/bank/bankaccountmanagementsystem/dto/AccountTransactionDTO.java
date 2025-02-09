package com.bank.bankaccountmanagementsystem.dto;

import com.bank.bankaccountmanagementsystem.model.AccountTransaction.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AccountTransactionDTO {
    private String accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    public AccountTransactionDTO(String accountId, TransactionType transactionType, BigDecimal amount, LocalDateTime transactionDate) {
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }
}
