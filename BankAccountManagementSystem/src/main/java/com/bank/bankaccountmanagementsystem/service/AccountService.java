package com.bank.bankaccountmanagementsystem.service;

import com.bank.bankaccountmanagementsystem.mapper.AccountMapper;
import com.bank.bankaccountmanagementsystem.dto.AccountDTO;
import com.bank.bankaccountmanagementsystem.model.Account;
import com.bank.bankaccountmanagementsystem.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }


    public AccountDTO createAccount(AccountDTO accountDTO) {
        Optional<Account> existingAccount = accountRepository.findByAccountOwnerIdentityNoAndAccountType(
                accountDTO.getAccountOwnerIdentityNo(), accountDTO.getAccountType());

        if (existingAccount.isPresent()) {
            throw new IllegalArgumentException("An account already exists with this identity number and account type.");
        }

        Account account = accountMapper.toAccountEntity(accountDTO);
        Account savedAccount = accountRepository.save(account);

        return accountMapper.toAccountDTO(savedAccount);
    }


    public AccountDTO updateAccount(UUID accountId, AccountDTO accountDTO) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        existingAccount.setAccountOwnerFirstName(accountDTO.getAccountOwnerFirstName());
        existingAccount.setAccountOwnerLastName(accountDTO.getAccountOwnerLastName());
        existingAccount.setBalance(accountDTO.getBalance());
        existingAccount.setAccountType(accountDTO.getAccountType());

        Account updatedAccount = accountRepository.save(existingAccount);
        return accountMapper.toAccountDTO(updatedAccount);
    }


    public AccountDTO getAccountDTOById(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        return accountMapper.toAccountDTO(account);
    }


    @Transactional
    public void deleteAccount(UUID accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }
        accountRepository.deleteById(accountId);
    }
}
