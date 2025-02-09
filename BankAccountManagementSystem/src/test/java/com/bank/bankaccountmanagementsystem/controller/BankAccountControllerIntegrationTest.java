package com.bank.bankaccountmanagementsystem.controller;

import com.bank.bankaccountmanagementsystem.dto.AccountDTO;
import com.bank.bankaccountmanagementsystem.model.Account.AccountType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }


    private long generateRandomIdentityNo() {
        Random random = new Random();
        long min = 10000000000L;
        long max = 99999999999L;
        return min + ((long) (random.nextDouble() * (max - min)));
    }

    @Test
    public void testCreateAccount() throws Exception {
        long randomIdentityNo = generateRandomIdentityNo();

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountOwnerIdentityNo(randomIdentityNo);
        accountDTO.setAccountOwnerFirstName("asd");
        accountDTO.setAccountOwnerLastName("Ddd");
        accountDTO.setAccountType(AccountType.TL);
        accountDTO.setBalance(BigDecimal.valueOf(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountOwnerIdentityNo").value(randomIdentityNo))
                .andExpect(jsonPath("$.accountOwnerFirstName").value("asd"))
                .andExpect(jsonPath("$.accountOwnerLastName").value("Ddd"))
                .andExpect(jsonPath("$.accountType").value("TL"))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        long randomIdentityNo = generateRandomIdentityNo();

        AccountDTO newAccount = new AccountDTO();
        newAccount.setAccountOwnerIdentityNo(randomIdentityNo);
        newAccount.setAccountOwnerFirstName("asd");
        newAccount.setAccountOwnerLastName("Ddd");
        newAccount.setAccountType(AccountType.TL);
        newAccount.setBalance(BigDecimal.valueOf(1000));

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = createResult.getResponse().getContentAsString();
        AccountDTO createdAccount = objectMapper.readValue(jsonResponse, AccountDTO.class);
        UUID accountId = createdAccount.getId();

        AccountDTO updatedAccount = new AccountDTO();
        updatedAccount.setAccountOwnerIdentityNo(randomIdentityNo);
        updatedAccount.setAccountOwnerFirstName("Ali");
        updatedAccount.setAccountOwnerLastName("Veli");
        updatedAccount.setAccountType(AccountType.TL);
        updatedAccount.setBalance(BigDecimal.valueOf(2000));

        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountOwnerFirstName").value("Ali"))
                .andExpect(jsonPath("$.accountOwnerLastName").value("Veli"))
                .andExpect(jsonPath("$.balance").value(2000));
    }

    @Test
    public void testDeleteAccount() throws Exception {
        long randomIdentityNo = generateRandomIdentityNo();

        AccountDTO newAccount = new AccountDTO();
        newAccount.setAccountOwnerIdentityNo(randomIdentityNo);
        newAccount.setAccountOwnerFirstName("asd");
        newAccount.setAccountOwnerLastName("Ddd");
        newAccount.setAccountType(AccountType.TL);
        newAccount.setBalance(BigDecimal.valueOf(1000));

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = createResult.getResponse().getContentAsString();
        AccountDTO createdAccount = objectMapper.readValue(jsonResponse, AccountDTO.class);
        UUID accountId = createdAccount.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Account deleted successfully."));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAccountById() throws Exception {
        long randomIdentityNo = generateRandomIdentityNo();

        AccountDTO newAccount = new AccountDTO();
        newAccount.setAccountOwnerIdentityNo(randomIdentityNo);
        newAccount.setAccountOwnerFirstName("John");
        newAccount.setAccountOwnerLastName("Doe");
        newAccount.setAccountType(AccountType.TL);
        newAccount.setBalance(BigDecimal.valueOf(1000));

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = createResult.getResponse().getContentAsString();
        AccountDTO createdAccount = objectMapper.readValue(jsonResponse, AccountDTO.class);
        UUID accountId = createdAccount.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountOwnerFirstName").value("John"))
                .andExpect(jsonPath("$.accountOwnerLastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(1000));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testDeposit() throws Exception {
        long randomIdentityNo = generateRandomIdentityNo();
        AccountDTO newAccount = new AccountDTO();
        newAccount.setAccountOwnerIdentityNo(randomIdentityNo);
        newAccount.setAccountOwnerFirstName("John");
        newAccount.setAccountOwnerLastName("Doe");
        newAccount.setAccountType(AccountType.TL);
        newAccount.setBalance(BigDecimal.valueOf(1000));

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = createResult.getResponse().getContentAsString();
        AccountDTO createdAccount = objectMapper.readValue(jsonResponse, AccountDTO.class);
        UUID accountId = createdAccount.getId();

        // Test deposit with valid amount
        BigDecimal depositAmount = BigDecimal.valueOf(500);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/" + accountId + "/deposit")
                        .param("amount", depositAmount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        String responseContent = result.getResponse().getContentAsString();


        assertTrue(responseContent.contains("Money deposited successfully. Transaction ID:"));


        assertTrue(responseContent.matches("Money deposited successfully. Transaction ID: \\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"));
    }

    @Test
    public void testWithdraw() throws Exception {
        long randomIdentityNo = generateRandomIdentityNo();
        AccountDTO newAccount = new AccountDTO();
        newAccount.setAccountOwnerIdentityNo(randomIdentityNo);
        newAccount.setAccountOwnerFirstName("John");
        newAccount.setAccountOwnerLastName("Doe");
        newAccount.setAccountType(AccountType.TL);
        newAccount.setBalance(BigDecimal.valueOf(1000));

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = createResult.getResponse().getContentAsString();
        AccountDTO createdAccount = objectMapper.readValue(jsonResponse, AccountDTO.class);
        UUID accountId = createdAccount.getId();


        BigDecimal withdrawAmount = BigDecimal.valueOf(500);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/" + accountId + "/withdraw")
                        .param("amount", withdrawAmount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        String responseContent = result.getResponse().getContentAsString();


        assertTrue(responseContent.contains("Money withdrawn successfully. Transaction ID:"));


        assertTrue(responseContent.matches("Money withdrawn successfully. Transaction ID: \\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"));
    }


}

