package com.endava.csvreport;

import com.endava.csvreport.csv.CsvCreator;
import com.endava.csvreport.csv.CsvWriter;
import com.endava.csvreport.database.ConnectionHandler;
import com.endava.csvreport.pojo.Report;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class CsvReportApplication {

	public static void main(String[] args) throws Exception {
        SpringApplication.run(CsvReportApplication.class, args);

		String address = "jdbc:postgresql://127.0.0.1:5432/postgres";

        CsvWriter writer = new CsvWriter();
        ConnectionHandler handler = new ConnectionHandler(DriverManager.getConnection(address, "postgres", ""));
        CsvCreator creator = new CsvCreator(handler);

        long timerStart = System.nanoTime();
        writer.writeCsvJackson(Report.class, creator.getAll());
        System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timerStart));

        Path path = Paths.get("openCsvReport.csv");
        timerStart = System.nanoTime();

        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        writer.writeCsvOpen(creator.getResultSet(), path);
        System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timerStart));

        Path pathBean = Paths.get("openCsvBeanReport.csv");
        timerStart = System.nanoTime();

        if (!Files.exists(pathBean)) {
            Files.createFile(pathBean);
        }
        writer.writeCsvOpenBean(creator, pathBean);
        System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timerStart));
    }
}
