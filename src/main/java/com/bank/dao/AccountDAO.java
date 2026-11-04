package com.bank.dao;

import com.bank.model.Account;
import com.bank.util.DBConnection;
import com.bank.exception.InsufficientBalanceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    //CREATE : INSERT A NEW ACCOUNT AND RETURNS ITS GENERATED ACCOUNT NUMBER
    public int createAccount(Account account) throws SQLException{
        String sql= "INSERT into accounts (holder_name,acc_type,balance) values (?,?,?)";
        try(Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, account.getHolderName());
            ps.setString(2, account.getAccType());
            ps.setDouble(3,account.getBalance());
            ps.executeUpdate();

            try(ResultSet keys=ps.getGeneratedKeys()){
                if(keys.next()){
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    //READ: fetch all accounts
    public List<Account> getAllAccounts() throws SQLException{
        List<Account> list = new ArrayList<>();
        String sql= "Select acc_id, holder_name ,acc_type, balance FROM accounts";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){
            while(rs.next()) {
                list.add(new Account(
                        rs.getInt("acc_id"),
                        rs.getString("holder_name"),
                        rs.getString("acc_type"),
                        rs.getDouble("balance")
                ));
            }
            }
        return list;
        }

        //READ ONE: find an account using its number
    public Account getAccountById(int accId) throws SQLException{
        String sql="Select acc_id, holder_name, acc_type,balance from accounts WHERE acc_id = ?";
        try(Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1, accId);

            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    return new Account(
                            rs.getInt("acc_id"),
                            rs.getString("holder_name"),
                            rs.getString("acc_type"),
                            rs.getDouble("balance")
                    );
                }
            }
        }
        return null;
    }

    public boolean  deposit(Connection con,int accId, double amount) throws SQLException{
        String sql = "UPDATE accounts SET balance = balance + ? WHERE acc_id = ?";

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, accId);
            return ps.executeUpdate() > 0;
        }
    }

    public void withdraw(Connection con,int accId, double amount) throws SQLException,InsufficientBalanceException{
        String sql="UPDATE accounts SET balance = balance - ? WHERE acc_id = ? AND balance >= ?  ";
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, accId);
            ps.setDouble(3, amount);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new InsufficientBalanceException("Insufficient balance for withdrawal of " + amount);
            }
        }
    }

    public boolean deleteAccount(Connection con, int accId) throws SQLException {
        String sql = "DELETE FROM accounts WHERE acc_id = ? AND balance = 0";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accId);
            return ps.executeUpdate() > 0;
        }
    }



    }






