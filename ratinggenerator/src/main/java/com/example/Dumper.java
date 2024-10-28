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
public class Dumper {
    private final Deque<String[]> ratings;
    private final String dumpFile;

    public void dumpCSV() {
        try (FileWriter writer = new FileWriter(dumpFile)) {
            for (String[] r : ratings) writer.write(String.join(",", r) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void dumpXLSX() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet 1");
        String[] header = { "TourID", "Hotel rating", "Beach rating", "Transportation rating", "Overall", "Recommendation" };
        int rowId = 0;
        XSSFRow row = sheet.createRow(rowId++);
        for (int i = 0; i < 6; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(header[i]);
        }
        for (String[] r : ratings) {
            row = sheet.createRow(rowId++);
            int cellId = 0;
            for (int i = 0; i < 6; i++) {
                Cell cell = row.createCell(cellId++);
                cell.setCellValue(r[i]);
            }
        }
        try (FileOutputStream out = new FileOutputStream(new File(dumpFile))) {
            workbook.write(out);
            workbook.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
}