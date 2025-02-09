package com.bank.bankaccountmanagementsystem.mapper;

import com.bank.bankaccountmanagementsystem.dto.AccountDTO;
import com.bank.bankaccountmanagementsystem.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {


    public AccountDTO toAccountDTO(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getAccountOwnerIdentityNo(),
                account.getAccountOwnerFirstName(),
                account.getAccountOwnerLastName(),
                account.getAccountType(),
                account.getBalance()
        );
    }


    public Account toAccountEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setAccountOwnerIdentityNo(accountDTO.getAccountOwnerIdentityNo());
        account.setAccountOwnerFirstName(accountDTO.getAccountOwnerFirstName());
        account.setAccountOwnerLastName(accountDTO.getAccountOwnerLastName());
        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());
        return account;
    }
}
