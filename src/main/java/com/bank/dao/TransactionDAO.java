package com.bank.dao;
import com.bank.model.Transaction;
import com.bank.util.DBConnection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDAO {
    public void addTransaction(Connection con,Transaction t) throws SQLException{
        String sql="INSERT into transactions(acc_id,transaction_type,amount) VALUES (?,?,?)";
        try(PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,t.getAccId());
            ps.setString(2,t.getTransactionType());
            ps.setDouble(3,t.getAmount());
            ps.executeUpdate();
        }
    }

    public List<Transaction> getTransactionsByAccount(int accId) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT transaction_id, acc_id, transaction_type, amount, transaction_date "
                + "FROM transactions WHERE acc_id = ? ORDER BY transaction_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getInt("acc_id"),
                            rs.getString("transaction_type"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("transaction_date").toLocalDateTime()));
                }
            }
        }
        return list;
    }

    public void deleteByAccount(Connection con, int accId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE acc_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accId);
            ps.executeUpdate();
        }
    }
}
