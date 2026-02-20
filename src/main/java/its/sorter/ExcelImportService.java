package its.sorter;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelImportService {

    public ImportResult importFile(File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {

            List<Row> sorted = readSorted(wb.getSheet("Sorted"));
            List<Row> candidates = readCandidates(wb.getSheet("Candidates"));

            return new ImportResult(sorted, candidates);
        }
    }

    private List<Row> readSorted(Sheet sheet) {
        if (sheet == null) return List.of();

        List<Row> list = new ArrayList<>();

        for (org.apache.poi.ss.usermodel.Row r : sheet) {

            // preskoči header red (ako postoji)
            if (r.getRowNum() == 0) continue;

            String letter = getCellValue(r, 0);
            String artist = getCellValue(r, 1);
            String song = getCellValue(r, 2);
            String type = getCellValue(r, 3);

            // preskoči potpuno prazne redove
            if (artist.isBlank()) continue;

            list.add(new Row(letter, artist, song, type));
        }

        return list;
    }

    private List<Row> readCandidates(Sheet sheet) {
        if (sheet == null) return List.of();

        List<Row> list = new ArrayList<>();

        for (org.apache.poi.ss.usermodel.Row r : sheet) {

            if (r.getRowNum() == 0) continue;

            String artist = getCellValue(r, 0);
            String song = getCellValue(r, 1);

            if (artist.isBlank()) continue;

            list.add(new Row("?", artist, song, ""));
        }

        return list;
    }

    private String getCellValue(org.apache.poi.ss.usermodel.Row row, int index) {
        if (row == null) return "";

        Cell cell = row.getCell(index, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                } else {
                    yield String.valueOf((long) cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}