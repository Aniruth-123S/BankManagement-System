package com.bank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private int transactionId;
    private int accId;
    private String transactionType;
    private double amount;
    private LocalDateTime transactionDate;

    // For a NEW transaction: MySQL sets the id and the date itself
    public Transaction(int accId, String transactionType, double amount) {
        this.accId = accId;
        this.transactionType = transactionType;
        this.amount = amount;
    }


    // For a transaction LOADED from the database (id and date already exist)
    public Transaction(int transactionId, int accId, String transactionType,
                       double amount, LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.accId = accId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getAccId() {
        return accId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    @Override
    public String toString() {
        String when = (transactionDate == null)
                ? "-"
                : transactionDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"));
        return "#" + transactionId + " | " + when + " | " + transactionType + " | " + amount;
    }

}
