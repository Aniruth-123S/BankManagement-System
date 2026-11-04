# Bank Account Management System

A console-based banking application built with **Core Java, JDBC and MySQL**. It supports creating accounts, deposits, withdrawals, money transfers, transaction history and account closure. Money movements run inside database transactions, so account balances and transaction history always stay consistent.

## Features
- Create an account (savings or current)
- Display all accounts and check a balance
- Deposit and withdraw money (overdrafts are blocked)
- Transfer money between accounts using `commit()` and `rollback()`
- View transaction history, newest first
- Close an account (allowed only when the balance is zero)
- Input validation and a custom `InsufficientBalanceException`

## Tech stack
- Java 21
- JDBC (MySQL Connector/J)
- MySQL 8
- Maven
- IntelliJ IDEA

## Project structure
```
src/main/java/com/bank
├── Main.java            (menu and user input)
├── model                (Account, Transaction)
├── dao                  (AccountDAO, TransactionDAO)
├── service              (BankService: transactions)
├── exception            (InsufficientBalanceException)
└── util                 (DBConnection)
```

## Database
Two tables: `accounts` and `transactions`. The full script is in `schema.sql`.

## How to run
1. Install Java 21, MySQL 8 and Maven.
2. Run `schema.sql` in MySQL to create the `bank_management` database and tables.
3. Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties` and enter your MySQL username and password.
4. Run `com.bank.Main` from your IDE.

## Concepts demonstrated
- JDBC with `PreparedStatement` (protection against SQL injection)
- DAO pattern and a service layer
- Transactions: `setAutoCommit(false)`, `commit()`, `rollback()`
- Try-with-resources
- Custom exceptions
- Atomic SQL (`balance >= ?` inside the UPDATE)

## Possible improvements
`BigDecimal` for money, connection pooling (HikariCP), soft delete for closed accounts, authentication, and automated JUnit tests.