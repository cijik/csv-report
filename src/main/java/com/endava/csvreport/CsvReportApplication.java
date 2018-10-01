package com.endava.csvreport;

import com.endava.csvreport.csv.CsvWriter;
import com.endava.csvreport.csv.ReportCsvCreator;
import com.endava.csvreport.database.ConnectionHandler;
import com.endava.csvreport.pojo.Report;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;

@SpringBootApplication
public class CsvReportApplication {

	public static void main(String[] args) throws Exception {
        SpringApplication.run(CsvReportApplication.class, args);

		String address = "jdbc:postgresql://127.0.0.1:5432/postgres";

        CsvWriter writer = new CsvWriter();
        ConnectionHandler handler = new ConnectionHandler(DriverManager.getConnection(address, "postgres", "postgres"));
        ReportCsvCreator creator = new ReportCsvCreator(handler);

        BasicEtmConfigurator.configure();
        EtmMonitor monitor = EtmManager.getEtmMonitor();
        monitor.start();

        for (int i = 0; i < 100; i++) {
            writer.writeCsvJackson(Report.class, creator.getReportList());
            Files.delete(Paths.get("payment.csv"));
        }

        for (int i = 0; i < 100; i++) {
            writer.writeCsvOpen(creator.getReportResultSet(), Paths.get("paymentOpen.csv"));
            Files.delete(Paths.get("paymentOpen.csv"));
        }

        for (int i = 0; i < 100; i++) {
            writer.writeCsvOpenBean(creator, Paths.get("paymentOpenBean.csv"));
            Files.delete(Paths.get("paymentOpenBean.csv"));
        }

        for (int i = 0; i < 100; i++) {
            writer.writeCsvUnivocity(creator.getReportList());
            Files.delete(Paths.get("paymentUni.csv"));
        }

        monitor.render(new SimpleTextRenderer());
        monitor.stop();
    }
}
