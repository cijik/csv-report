package com.endava.csvreport.csv;

import com.endava.csvreport.pojo.Report;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.conversions.Conversions;
import com.univocity.parsers.fixed.FieldAlignment;
import com.univocity.parsers.fixed.FixedWidthFields;
import com.univocity.parsers.fixed.FixedWidthWriter;
import com.univocity.parsers.fixed.FixedWidthWriterSettings;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.util.List;
import java.util.Locale;

public class CsvWriter {

    private CsvMapper mapper;

    private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    public CsvWriter() {
        mapper = new CsvMapper();
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
    }

    public void writeCsvJackson(Class clazz, List<?> report) throws IOException {
        EtmPoint point = etmMonitor.createPoint("CsvWriter:writeCsvJackson");

        CsvSchema schema = mapper.schemaFor(clazz).withHeader();
        ObjectWriter myObjectWriter = mapper.writer(schema);
        File tempFile = new File("payment.csv");
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile, true);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, StandardCharsets.UTF_8);
        myObjectWriter.writeValue(writerOutputStream, report);

        point.collect();
    }

    public void writeCsvUnivocity(List<Report> report) throws FileNotFoundException {
        EtmPoint point = etmMonitor.createPoint("CsvWriter:writeCsvUnivocity");

        File tempFile = new File("paymentUni.csv");
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile, true);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, StandardCharsets.UTF_8);

        FixedWidthFields fields = new FixedWidthFields();
        fields.addField("payment_id",5,FieldAlignment.RIGHT, '0');
        fields.addField("customer_id",5,FieldAlignment.RIGHT, '0');
        fields.addField("staff_id",5,FieldAlignment.RIGHT, '0');
        fields.addField("rental_id", 5, FieldAlignment.RIGHT, '0');
        fields.addField("amount",5, FieldAlignment.RIGHT, '0');
        fields.addField("payment_date", 20, FieldAlignment.RIGHT, '_');
        FixedWidthWriterSettings settings = new FixedWidthWriterSettings(fields);

        BeanWriterProcessor processor = new BeanWriterProcessor<>(Report.class);
        settings.setRowWriterProcessor(processor);
        processor.convertFields(Conversions.toDate(Locale.ENGLISH, "yyyy-MMM-dd")).add("payment_date");

        FixedWidthWriter writer = new FixedWidthWriter(writerOutputStream, settings);

        writer.processRecords(report);

        writer.close();

        point.collect();
    }

    public void writeCsvOpen(ResultSet report, Path path) throws Exception {
        EtmPoint point = etmMonitor.createPoint("CsvWriter:writeCsvOpen");

        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));
        writer.writeAll(report, true);
        writer.close();

        point.collect();
    }

    public void writeCsvOpenBean(ReportCsvCreator creator, Path path) throws Exception {
        EtmPoint point = etmMonitor.createPoint("CsvWriter:writeCsvOpenBean");

        Writer writer = new FileWriter(path.toString());

        StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();

        List<Report> list = creator.getReportList();

        sbc.write(list);
        writer.close();
        point.collect();
    }
}
