package com.bank;

import com.bank.service.BankService;
import com.bank.dao.AccountDAO;
import com.bank.model.Account;
import com.bank.exception.InsufficientBalanceException;
import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final AccountDAO accountDAO =new AccountDAO();
    private static final TransactionDAO transactionDAO = new TransactionDAO();
    private static final Scanner sc=new Scanner(System.in);
    private static final BankService bankService = new BankService();

    public static void main(String[] args) {



       int choice;

       do{
           printMenu();
           choice =readInt("Enter your choice:");

           switch (choice){
               case 1:
                   createAccount();
                   break;
               case 2:
                   displayAccounts();
                   break;
               case 3:
                   deposit();
                   break;
               case 4:
                   withdraw();
                   break;
               case 5:
                   checkBalance();
                   break;
               case 6:
                   transfer();
                   break;
               case 7:
                   viewHistory();
                   break;
               case 8:
                   closeAccount();
                   break;
               case 0:
                   System.out.println("Thank you for using the Bank Management System!..");
                   break;
               default:
                   System.out.println("Invalid choice try again later");
           }
       }while(choice!=0);

    }

    private static void printMenu() {
        System.out.println();
        System.out.println("===== BANK MANAGEMENT SYSTEM =====");
        System.out.println("1. Create account");
        System.out.println("2. Display all accounts");
        System.out.println("3. Deposit money");
        System.out.println("4. Withdraw money");
        System.out.println("5. Check balance");
        System.out.println("6. Transfer money");
        System.out.println("7. View transaction history");
        System.out.println("8. Close account");
        System.out.println("0. Exit");
        System.out.println("==================================");
    }

    private static void createAccount(){
        System.out.println("Enter holder name: ");
        String name =sc.nextLine().trim();

        System.out.println("Enter account type (SAVINGS/CURRENT): ");
        String type= sc.nextLine().trim().toUpperCase();

        double balance= readDouble("Enter opening balance: ");

        if(name.isEmpty()){
            System.out.println("Name cannot be empty.");
            return;
        }
        if(!type.equals("SAVINGS") && !type.equals("CURRENT")){
            System.out.println("Account type must be SAVINGS or CURRENT.");
            return;
        }
        if(balance<0){
            System.out.println("Balance must be a number greater than 0");
            return;
        }

        try{
            Account account=new Account(name,type,balance);
            int accId = accountDAO.createAccount(account);
            System.out.println("Account created successfully! Your account number is : " + accId);

        }
        catch (SQLException e){
            System.out.println("Could not create account: " + e.getMessage());
        }
    }

    private static void displayAccounts(){
        try{
            List<Account> accounts =accountDAO.getAllAccounts();
            if(accounts.isEmpty()){
                System.out.println("No accounts found");
                return;
            }
            System.out.println("---- All Accounts ----");
            for (Account acc : accounts) {
                System.out.println(acc);
            }
        } catch (SQLException e) {
            System.out.println("Could not read accounts: " + e.getMessage());
        }
    }

    private static void deposit() {
        int accId = readInt("Enter account number: ");
        double amount = readDouble("Enter amount to deposit: ");

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        try {
            boolean done = bankService.deposit(accId, amount);
            if (!done) {
                System.out.println("Account not found.");
                return;
            }
            Account updated = accountDAO.getAccountById(accId);
            System.out.println("Deposit successful. New balance: " + updated.getBalance());
        } catch (SQLException e) {
            System.out.println("Could not deposit: " + e.getMessage());
        }
    }

    private static void withdraw() {
        int accId = readInt("Enter account number: ");
        double amount = readDouble("Enter amount to withdraw: ");

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        try {
            Account existing = accountDAO.getAccountById(accId);
            if (existing == null) {
                System.out.println("Account not found.");
                return;
            }

            bankService.withdraw(accId, amount);

            Account updated = accountDAO.getAccountById(accId);
            System.out.println("Withdrawal successful. New balance: " + updated.getBalance());
        } catch (InsufficientBalanceException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Could not withdraw: " + e.getMessage());
        }
    }

    private static void checkBalance() {
        int accId = readInt("Enter account number: ");

        try {
            Account acc = accountDAO.getAccountById(accId);
            if (acc == null) {
                System.out.println("Account not found.");
                return;
            }
            System.out.println("Holder: " + acc.getHolderName() + " | Balance: " + acc.getBalance());
        } catch (SQLException e) {
            System.out.println("Could not read balance: " + e.getMessage());
        }
    }

    private static void transfer() {
        int fromId = readInt("Enter sender account number: ");
        int toId = readInt("Enter receiver account number: ");
        double amount = readDouble("Enter amount to transfer: ");

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }
        if (fromId == toId) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        try {
            if (accountDAO.getAccountById(fromId) == null) {
                System.out.println("Sender account not found.");
                return;
            }
            if (accountDAO.getAccountById(toId) == null) {
                System.out.println("Receiver account not found.");
                return;
            }

            bankService.transfer(fromId, toId, amount);

            Account sender = accountDAO.getAccountById(fromId);
            Account receiver = accountDAO.getAccountById(toId);
            System.out.println("Transfer successful.");
            System.out.println("Sender " + fromId + " balance: " + sender.getBalance());
            System.out.println("Receiver " + toId + " balance: " + receiver.getBalance());

        } catch (InsufficientBalanceException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Could not transfer: " + e.getMessage());
        }
    }
    private static void viewHistory() {
        int accId = readInt("Enter account number: ");

        try {
            if (accountDAO.getAccountById(accId) == null) {
                System.out.println("Account not found.");
                return;
            }

            List<Transaction> history = transactionDAO.getTransactionsByAccount(accId);

            if (history.isEmpty()) {
                System.out.println("No transactions found for this account.");
                return;
            }

            System.out.println("---- Transaction history for account " + accId + " (latest first) ----");
            for (Transaction t : history) {
                System.out.println(t);
            }
        } catch (SQLException e) {
            System.out.println("Could not read history: " + e.getMessage());
        }
    }

    private static void closeAccount() {
        int accId = readInt("Enter account number to close: ");

        try {
            Account acc = accountDAO.getAccountById(accId);
            if (acc == null) {
                System.out.println("Account not found.");
                return;
            }
            if (acc.getBalance() != 0) {
                System.out.println("Cannot close account. Balance must be 0. Current balance: "
                        + acc.getBalance());
                return;
            }

            System.out.print("This permanently deletes the account and its history. Type YES to confirm: ");
            String confirm = sc.nextLine().trim();
            if (!confirm.equals("YES")) {
                System.out.println("Cancelled. The account was not closed.");
                return;
            }

            bankService.closeAccount(accId);
            System.out.println("Account " + accId + " closed successfully.");

        } catch (SQLException e) {
            System.out.println("Could not close account: " + e.getMessage());
        }
    }


    private static int readInt(String message) {
        System.out.print(message);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double readDouble(String message) {
        System.out.print(message);
        try {
            return Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}

