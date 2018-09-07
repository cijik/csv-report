package com.endava.csvreport.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionHandler {

    private Connection connection;

    public ConnectionHandler(Connection connection) {
        this.connection = connection;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("Cannot perform SQL operation");
        }
    }

    public ResultSet get(String sql) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
