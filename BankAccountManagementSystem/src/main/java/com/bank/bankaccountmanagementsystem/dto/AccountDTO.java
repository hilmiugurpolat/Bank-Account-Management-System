package com.bank.bankaccountmanagementsystem.dto;

import lombok.*;
import com.bank.bankaccountmanagementsystem.model.Account.AccountType;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private UUID id;
    private Long accountOwnerIdentityNo;
    private String accountOwnerFirstName;
    private String accountOwnerLastName;
    private AccountType accountType;
    private BigDecimal balance;
}
