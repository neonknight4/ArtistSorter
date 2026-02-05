/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package its.sorter;

import java.io.File;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

/**
 *
 * @author mihailo-jankovic
 */
public class ExcelExportService {

    public void export(List<Row> sorted, List<Row> candidates, File file) throws Exception {
        Workbook wb = new XSSFWorkbook();

        writeSorted(wb.createSheet("Sorted"), sorted);
        writeCandidates(wb.createSheet("Candidates"), candidates);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            wb.write(fos);
        }

        wb.close();
    }

    private void writeSorted(Sheet sheet, List<Row> data) {
        int r = 0;
        for (Row row : data) {
            org.apache.poi.ss.usermodel.Row x = sheet.createRow(r++);
            x.createCell(0).setCellValue(row.getLetter());
            x.createCell(1).setCellValue(row.getArtist());
            x.createCell(2).setCellValue(row.getSong());
            x.createCell(3).setCellValue(row.getType());
        }
    }

    private void writeCandidates(Sheet sheet, List<Row> data) {
        int r = 0;
        for (Row row : data) {
            org.apache.poi.ss.usermodel.Row x = sheet.createRow(r++);
            x.createCell(0).setCellValue(row.getArtist());
            x.createCell(1).setCellValue(row.getSong());
        }
    }
}
