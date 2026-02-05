/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package its.sorter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author mihailo-jankovic
 */
public class ExcelImportService {

    public ImportResult importFile(File file) throws Exception {
        try (Workbook wb = new XSSFWorkbook(file)) {
            List<Row> sorted = readSorted(wb.getSheet("Sorted"));
            List<Row> candidates = readCandidates(wb.getSheet("Candidates"));
            return new ImportResult(sorted, candidates);
        }
    }

    private List<Row> readSorted(Sheet sheet) {
        if (sheet == null) {
            return List.of();
        }

        List<Row> list = new ArrayList<>();
        for (org.apache.poi.ss.usermodel.Row r : sheet) {
            list.add(new Row(
                    r.getCell(0).getStringCellValue(),
                    r.getCell(1).getStringCellValue(),
                    r.getCell(2).getStringCellValue(),
                    r.getCell(3).getStringCellValue()
            ));
        }
        return list;
    }

    private List<Row> readCandidates(Sheet sheet) {
        if (sheet == null) {
            return List.of();
        }

        List<Row> list = new ArrayList<>();
        for (org.apache.poi.ss.usermodel.Row r : sheet) {
            list.add(new Row("?",
                    r.getCell(0).getStringCellValue(),
                    r.getCell(1).getStringCellValue(),
                    ""
            ));
        }
        return list;
    }

}
