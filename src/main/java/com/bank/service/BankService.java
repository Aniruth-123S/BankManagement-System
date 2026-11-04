package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;
import com.bank.util.DBConnection;

import com.bank.exception.InsufficientBalanceException;

import java.sql.Connection;
import java.sql.SQLException;

public class BankService {

    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public boolean deposit(int accId, double amount) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                boolean updated = accountDAO.deposit(con, accId, amount);
                if (!updated) {
                    con.rollback();
                    return false;
                }
                transactionDAO.addTransaction(con, new Transaction(accId, "DEPOSIT", amount));
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public void withdraw(int accId,double amount) throws SQLException,InsufficientBalanceException{
        try(Connection con=DBConnection.getConnection()){
            con.setAutoCommit(false);
            try{
                accountDAO.withdraw(con,accId,amount);
                transactionDAO.addTransaction(con,new Transaction(accId,"WITHDRAW",amount));
                con.commit();
            }
            catch (SQLException | InsufficientBalanceException e) {
                con.rollback();
                throw e;
            }
        }
    }
    public void transfer(int fromAccId, int toAccId, double amount)
            throws SQLException, InsufficientBalanceException {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                accountDAO.withdraw(con, fromAccId, amount);

                boolean credited = accountDAO.deposit(con, toAccId, amount);
                if (!credited) {
                    throw new SQLException("Receiver account not found: " + toAccId);
                }

                transactionDAO.addTransaction(con, new Transaction(fromAccId, "TRANSFER_OUT", amount));
                transactionDAO.addTransaction(con, new Transaction(toAccId, "TRANSFER_IN", amount));

                con.commit();
            } catch (SQLException | InsufficientBalanceException e) {
                con.rollback();
                throw e;
            }
        }


    }
    public void closeAccount(int accId) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                transactionDAO.deleteByAccount(con, accId);

                boolean deleted = accountDAO.deleteAccount(con, accId);
                if (!deleted) {
                    throw new SQLException("Account could not be closed: not found or balance is not zero");
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }



        }

