package com.bank.bankaccountmanagementsystem.service;

import com.bank.bankaccountmanagementsystem.dto.AccountDTO;
import com.bank.bankaccountmanagementsystem.mapper.AccountMapper;
import com.bank.bankaccountmanagementsystem.model.Account;
import com.bank.bankaccountmanagementsystem.repository.AccountRepository;
import com.bank.bankaccountmanagementsystem.repository.AccountTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountTransactionRepository accountTransactionRepository;

    private AccountDTO accountDTO;
    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountDTO = new AccountDTO();
        accountDTO.setAccountOwnerIdentityNo(12345678901L);
        accountDTO.setAccountOwnerFirstName("John");
        accountDTO.setAccountOwnerLastName("Doe");
        accountDTO.setBalance(BigDecimal.valueOf(1000.0));
        accountDTO.setAccountType(Account.AccountType.TL);

        account = new Account();
        account.setId(UUID.randomUUID());
        account.setAccountOwnerIdentityNo(accountDTO.getAccountOwnerIdentityNo());
        account.setAccountOwnerFirstName(accountDTO.getAccountOwnerFirstName());
        account.setAccountOwnerLastName(accountDTO.getAccountOwnerLastName());
        account.setBalance(accountDTO.getBalance());
        account.setAccountType(accountDTO.getAccountType());
    }

    @Test
    void createAccount_shouldCreateAccountSuccessfully() {

        when(accountRepository.findByAccountOwnerIdentityNoAndAccountType(accountDTO.getAccountOwnerIdentityNo(), accountDTO.getAccountType()))
                .thenReturn(Optional.empty());
        when(accountMapper.toAccountEntity(accountDTO)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toAccountDTO(account)).thenReturn(accountDTO);

        AccountDTO createdAccount = accountService.createAccount(accountDTO);

        assertNotNull(createdAccount);
        assertEquals(accountDTO.getAccountOwnerFirstName(), createdAccount.getAccountOwnerFirstName());
        assertEquals(accountDTO.getAccountOwnerLastName(), createdAccount.getAccountOwnerLastName());
    }

    @Test
    void createAccount_shouldThrowExceptionIfAccountExists() {

        when(accountRepository.findByAccountOwnerIdentityNoAndAccountType(accountDTO.getAccountOwnerIdentityNo(), accountDTO.getAccountType()))
                .thenReturn(Optional.of(account));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(accountDTO);
        });
        assertEquals("An account already exists with this identity number and account type.", exception.getMessage());
    }

    @Test
    void updateAccount_shouldUpdateAccountSuccessfully() {

        UUID accountId = account.getId();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toAccountDTO(account)).thenReturn(accountDTO);

        AccountDTO updatedAccount = accountService.updateAccount(accountId, accountDTO);

        assertNotNull(updatedAccount);
        assertEquals(accountDTO.getAccountOwnerFirstName(), updatedAccount.getAccountOwnerFirstName());
    }

    @Test
    void updateAccount_shouldThrowExceptionIfAccountNotFound() {

        UUID accountId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.updateAccount(accountId, accountDTO);
        });
        assertEquals("Account not found: " + accountId, exception.getMessage());
    }

    @Test
    void getAccountDTOById_shouldReturnAccountDTO() {

        UUID accountId = account.getId();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountMapper.toAccountDTO(account)).thenReturn(accountDTO);

        AccountDTO fetchedAccount = accountService.getAccountDTOById(accountId);

        assertNotNull(fetchedAccount);
        assertEquals(accountDTO.getAccountOwnerFirstName(), fetchedAccount.getAccountOwnerFirstName());
    }

    @Test
    void getAccountDTOById_shouldThrowExceptionIfAccountNotFound() {

        UUID accountId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.getAccountDTOById(accountId);
        });
        assertEquals("Account not found: " + accountId, exception.getMessage());
    }

    @Test
    void deleteAccount_shouldDeleteAccountSuccessfully() {
        UUID accountId = account.getId();

        when(accountRepository.existsById(accountId)).thenReturn(true);

        doNothing().when(accountTransactionRepository).deleteByAccountId(accountId);

        accountService.deleteAccount(accountId);

    }

    @Test
    void deleteAccount_shouldThrowExceptionIfAccountNotFound() {

        UUID accountId = UUID.randomUUID();
        when(accountRepository.existsById(accountId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deleteAccount(accountId);
        });
        assertEquals("Account not found: " + accountId, exception.getMessage());
    }
}
