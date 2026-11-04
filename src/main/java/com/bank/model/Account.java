package com.bank.model;

public class Account {
    private int accId;
    private String holderName;
    private String accType;
    private double balance;

    //CONSTRUCTOR 1:FOR A NEW ACCOUNT
    // FOR A NEW ACCOUNT DATABASE WLL GENERATE THE ID
    public Account(String holderName,String accType,double balance){
        this.holderName=holderName;
        this.accType=accType;
        this.balance=balance;
    }

    public  Account(int accId,String holderName,String accType,double balance){
        this.accId=accId;
        this.holderName=holderName;
        this.accType=accType;
        this.balance=balance;

    }

    public int getAccId() {
        return accId;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getAccType() {
        return accType;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString(){
        return "Account No: " + accId + " | " + holderName + " | " + accType + " | " + balance;
    }


}
