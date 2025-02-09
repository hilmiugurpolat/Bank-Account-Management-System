package com.bank.bankaccountmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Account owner identity number cannot be null")
    @Column(name = "account_owner_identity_number", unique = true, nullable = false)
    private Long accountOwnerIdentityNo;

    @NotNull(message = "Account owner's first name cannot be null")
    @Size(min = 1, max = 50)
    @Column(name = "account_owner_first_name", length = 50, nullable = false)
    private String accountOwnerFirstName;

    @NotNull(message = "Account owner's last name cannot be null")
    @Size(min = 1, max = 50)
    @Column(name = "account_owner_last_name", length = 50, nullable = false)
    private String accountOwnerLastName;

    @NotNull(message = "Account type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @DecimalMin(value = "0.00", message = "Balance cannot be less than 0")
    @DecimalMax(value = "9999999.99", message = "Balance cannot exceed the maximum limit")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    public enum AccountType {
        TL,
        USD,
        GBP
    }
}
