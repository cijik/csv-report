package com.endava.csvreport.csv;

import com.endava.csvreport.pojo.Report;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.sun.deploy.ref.Helpers;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CsvWriter {

    private CsvMapper mapper;
    private CsvSchema schema;

    public CsvWriter() {
        mapper = new CsvMapper();
    }

    public void writeCsvJackson(Class clazz, List<Report> report) throws IOException {
        schema = mapper.schemaFor(clazz).withHeader();
        ObjectWriter myObjectWriter = mapper.writer(schema);
        File tempFile = new File("payment.csv");
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, StandardCharsets.UTF_8);
        myObjectWriter.writeValue(writerOutputStream, report);
    }

    public void writeCsvOpen(ResultSet report, Path path) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));
        writer.writeAll(report, true);
        writer.close();
    }

    public void writeCsvOpenBean(CsvCreator creator, Path path) throws Exception {

        Writer writer = new FileWriter(path.toString());

        StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();

        List<Report> list = creator.getAll();

        sbc.write(list);
        writer.close();
    }
}
