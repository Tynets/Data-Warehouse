package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Deque;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Dumper implements Runnable {
    private final Deque<String[]> ratings;
    private final String type;
    private final String dumpFile;
    private final long numberOfRows;

    private void dumpCSV() {
        try (FileWriter writer = new FileWriter(dumpFile)) {
            writer.write("TourID,ClientID,Hotel rating,Beach rating,Transportation rating,Overall,Recommendation\n");
            long i = 0;
            for (String[] r : ratings) {
                if (i == numberOfRows) break;
                writer.write(String.join(",", r) + "\n");
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void dumpXLSX() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet 1");
        String[] header = { "TourID", "ClientID", "Hotel rating", "Beach rating", "Transportation rating", "Overall", "Recommendation" };
        int rowId = 0;
        XSSFRow row = sheet.createRow(rowId++);
        for (int i = 0; i < 7; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(header[i]);
        }
        long j = 0;
        for (String[] r : ratings) {
            if (j == numberOfRows) break;
            row = sheet.createRow(rowId++);
            int cellId = 0;
            for (int i = 0; i < 7; i++) {
                Cell cell = row.createCell(cellId++);
                cell.setCellValue(r[i]);
            }
            j++;
        }
        try (FileOutputStream out = new FileOutputStream(new File(dumpFile))) {
            workbook.write(out);
            workbook.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
    @Override
    public void run() {
        if (type.equals("xlsx")) dumpXLSX();
        else dumpCSV();
    }
}