# Bank Account Management System

This project is a simple system to manage bank accounts. It allows you to create, update, delete accounts, manage balances, and perform deposit/withdrawal operations.

## Features

- **Account Management**: You can create, update, delete, and query bank accounts.
- **Transaction Management**: You can deposit money into accounts and withdraw money from accounts.
- **Balance Management**: Balance is updated with each transaction. The balance cannot go below zero and cannot exceed 9,999,999.

## Technologies Used

- **Java 17**: Used as the core programming language.
- **Spring Boot 3.4.2**: Used as the application framework.
- **Swagger**: Used for API documentation and testing.
- **Lombok**: Used to reduce boilerplate code in Java.
- **JpaRepository, CrudRepository**: Used for database operations.
- **Mapstruct**: Used for converting between DTOs and entities.
- **MySQL**: Database options.
- **Liquibase**: Used for database migrations.
- **Maven**: Used for project management and build processes.
- **Unit Testing & Integration Testing**: Used to ensure the correctness of the system.

## API Endpoints

### 1. Create Account

To create an account, a **POST** request is sent. This operation creates a new bank account.

**Endpoint**: `POST /accounts`

**Request Example (JSON):**:
```json
{
    "accountOwnerIdentityNo": 12345678910,
    "accountOwnerFirstName": "hilmi uğur",
    "accountOwnerLastName": "polat",
    "accountType": "TL",
    "balance": 2000.00
}
```

**Response Example**:
```json
{
    "id": "c356ce6c-3e4b-40d4-af75-78330ca32d7e",
    "accountOwnerIdentityNo": 12345678910,
    "accountOwnerFirstName": "hilmi uğur",
    "accountOwnerLastName": "polat",
    "accountType": "TL",
    "balance": 2000.00
}
```
![image](https://github.com/user-attachments/assets/b7911d19-6566-4069-934c-e4843647f986)

### 2. Update Account

To update an existing account, a **PUT** request is sent with the updated account details.

**Endpoint**: PUT /accounts/{accountId}

**Request Example (JSON):**
```json
{
    "accountOwnerIdentityNo": 12345678910,
    "accountOwnerFirstName": "hilmi uğur",
    "accountOwnerLastName": "polat",
    "accountType": "USD",
    "balance": 3000.00
}
```
**Response Example**:
```json
{
    "id": "c356ce6c-3e4b-40d4-af75-78330ca32d7e",
    "accountOwnerIdentityNo": 12345678910,
    "accountOwnerFirstName": "hilmi uğur",
    "accountOwnerLastName": "polat",
    "accountType": "USD",
    "balance": 3000.00
}
```
![image](https://github.com/user-attachments/assets/5c389583-19f0-4652-9cad-39bba6cb8fe5)

### 3. Get Account by ID
To retrieve an account's details by its ID, a **GET** request is sent.

**Endpoint**: GET /accounts/{accountId}

**Response Example (JSON):**

```json
{
    "id": "c356ce6c-3e4b-40d4-af75-78330ca32d7e",
    "accountOwnerIdentityNo": 12345678910,
    "accountOwnerFirstName": "hilmi uğur",
    "accountOwnerLastName": "polat",
    "accountType": "USD",
    "balance": 3000.00
}
```
![image](https://github.com/user-attachments/assets/75eba3f0-43fc-4161-8227-9b9a6e9d089e)

### 4. Deposit Money

To deposit money into an account, a **POST** request is sent with the amount to be deposited.

**Endpoint**: POST /accounts/{accountId}/deposit

**Request Example:**

URL parameter: accountId (e.g., c356ce6c-3e4b-40d4-af75-78330ca32d7e)

Request parameter: amount=500.00

**Response Example**:
```
Money deposited successfully. Transaction ID: 2025-02-09T18:25:19.396502600
```
![image](https://github.com/user-attachments/assets/05bc79b9-cd34-4d95-a735-0bec4ee03220)


### 5. Withdraw Money

To withdraw money from an account, a **POST** request is sent with the amount to be withdrawn.

**Endpoint:** POST /accounts/{accountId}/withdraw

**Request Example:**

URL parameter: accountId (e.g., c356ce6c-3e4b-40d4-af75-78330ca32d7e)

Request parameter: amount=300.00

**Response Example**:
```
Money withdrawn successfully. Transaction ID: 2025-02-09T18:28:37.340209300
```

![image](https://github.com/user-attachments/assets/b731bef8-cfb9-4c01-ac42-2a07a3f2a7ca)


### 6. Delete Account
To delete an existing account, a **DELETE** request is sent with the account ID.

**Endpoint**: DELETE /accounts/{accountId}

Response Example (JSON):
```json
{
    "message": "Account deleted successfully."
}
```
![image](https://github.com/user-attachments/assets/7acfe591-c58b-43af-9951-483c65c6ad3e)
