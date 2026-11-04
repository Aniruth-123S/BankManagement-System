CREATE DATABASE IF NOT EXISTS bank_management;
USE bank_management;

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
    acc_id      INT AUTO_INCREMENT PRIMARY KEY,
    holder_name VARCHAR(50) NOT NULL,
    acc_type    VARCHAR(10) NOT NULL,
    balance     DECIMAL(12,2) NOT NULL DEFAULT 0.00
) AUTO_INCREMENT = 1001;

CREATE TABLE transactions (
    transaction_id   INT AUTO_INCREMENT PRIMARY KEY,
    acc_id           INT NOT NULL,
    transaction_type VARCHAR(15) NOT NULL,
    amount           DECIMAL(12,2) NOT NULL,
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (acc_id) REFERENCES accounts(acc_id)
);

SHOW TABLES;
DESC accounts;