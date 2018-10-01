package com.endava.csvreport.csv;

import com.endava.csvreport.database.ConnectionHandler;
import com.endava.csvreport.pojo.Report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportCsvCreator {

    private ConnectionHandler connectionHandler;

    public ReportCsvCreator(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public ResultSet getReportResultSet() {
        String request = "SELECT * FROM payment";
        return connectionHandler.get(request);
    }

    public List<Report> getReportList() throws SQLException {
        List<Report> reportList = new ArrayList<>();
        ResultSet rs = getReportResultSet();
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
        return new ArrayList<>(reportList);
    }
}
