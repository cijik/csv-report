package com.endava.csvreport.csv;

import com.endava.csvreport.database.ConnectionHandler;
import com.endava.csvreport.pojo.Report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CsvCreator {

    private ConnectionHandler connectionHandler;

    public CsvCreator(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public ResultSet getResultSet() {
        String request = "SELECT * FROM payment";
        return connectionHandler.get(request);
    }

    public List<Report> getAll() throws SQLException {
        List<Report> reportList = new ArrayList<>();
        ResultSet rs = getResultSet();
        while (rs.next()) {
            Report report = new Report();
            report.setPaymentId(rs.getInt(1));
            report.setCustomerId(rs.getInt(2));
            report.setStaffId(rs.getInt(3));
            report.setRentalId(rs.getInt(4));
            report.setAmount(rs.getInt(5));
            report.setPaymentDate(rs.getDate(6));
            reportList.add(report);
        }
        return reportList;
    }
}
