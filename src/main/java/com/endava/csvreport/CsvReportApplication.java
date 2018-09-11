package com.endava.csvreport;

import com.endava.csvreport.csv.CsvCreator;
import com.endava.csvreport.csv.CsvWriter;
import com.endava.csvreport.database.ConnectionHandler;
import com.endava.csvreport.pojo.Report;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;

@SpringBootApplication
public class CsvReportApplication {

	public static void main(String[] args) throws Exception {
        SpringApplication.run(CsvReportApplication.class, args);

		String address = "jdbc:postgresql://127.0.0.1:5432/postgres";

        CsvWriter writer = new CsvWriter();
        ConnectionHandler handler = new ConnectionHandler(DriverManager.getConnection(address, "postgres", "postgres"));
        CsvCreator creator = new CsvCreator(handler);

        Path path = Paths.get("openCsvReport.csv");
        Path pathBean = Paths.get("openCsvBeanReport.csv");

        BasicEtmConfigurator.configure();
        EtmMonitor monitor = EtmManager.getEtmMonitor();
        monitor.start();

        for (int i = 0; i < 100; i++) {
            writer.writeCsvJackson(Report.class, creator.getAll());
            Files.delete(Paths.get("payment.csv"));
        }

        for (int i = 0; i < 100; i++) {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            writer.writeCsvOpen(creator.getResultSet(), path);
            Files.delete(path);
        }

        for (int i = 0; i < 100; i++) {
            if (!Files.exists(pathBean)) {
                Files.createFile(pathBean);
            }
            writer.writeCsvOpenBean(creator, pathBean);
            Files.delete(pathBean);
        }

        monitor.render(new SimpleTextRenderer());
        monitor.stop();
    }
}
