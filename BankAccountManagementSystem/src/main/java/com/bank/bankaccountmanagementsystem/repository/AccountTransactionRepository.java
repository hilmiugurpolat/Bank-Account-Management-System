package com.bank.bankaccountmanagementsystem.repository;

import com.bank.bankaccountmanagementsystem.model.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, UUID> {

    void deleteByAccountId(UUID accountId);

}
