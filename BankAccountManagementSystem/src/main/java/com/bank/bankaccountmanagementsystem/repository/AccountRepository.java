package com.bank.bankaccountmanagementsystem.repository;

import com.bank.bankaccountmanagementsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByAccountOwnerIdentityNoAndAccountType(Long identityNo, Account.AccountType accountType);


    void deleteById(UUID accountId);
}
