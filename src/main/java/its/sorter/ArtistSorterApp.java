package its.sorter;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.*;

public class ArtistSorterApp extends Application {

    private final ObservableList<Row> autoSorted = FXCollections.observableArrayList();
    private final ObservableList<Row> candidates = FXCollections.observableArrayList();

    private final ArtistParserService parserService = new ArtistParserService();
    private final ExcelExportService exportService = new ExcelExportService();
    private final ExcelImportService importService = new ExcelImportService();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TextArea inputArea = new TextArea();

        Button processBtn = new Button("Process");
        processBtn.setOnAction(e -> processInput(inputArea.getText()));

        Button exportBtn = new Button("Export XLSX");
        exportBtn.setOnAction(e -> export(stage));

        Button importBtn = new Button("Import XLSX");
        importBtn.setOnAction(e -> importFile(stage));

        Button clearBtn = new Button("Clear");

        clearBtn.setOnAction(e -> clearAll());

        HBox buttons = new HBox(10, processBtn, exportBtn, importBtn, clearBtn);

        TableView<Row> autoTable = createAutoTable();
        autoTable.setItems(autoSorted);

        TableView<Row> candidateTable = createCandidateTable();
        candidateTable.setItems(candidates);

        VBox left = new VBox(10, new Label("Input"), inputArea, buttons);
        left.setPadding(new Insets(10));
        left.setPrefWidth(350);

        VBox right = new VBox(10,
                new Label("Sorted"), autoTable,
                new Label("Candidates"), candidateTable
        );
        right.setPadding(new Insets(10));

        Scene scene = new Scene(new HBox(left, right), 1100, 650);
        stage.setTitle("Artist Sorter");
        stage.setScene(scene);
        stage.show();
    }

    private void processInput(String text) {

        if (text == null || text.isBlank()) {
            return;
        }

        ImportResult parsed = parserService.parse(text);

        // ===== deduplikacija prema postojećem stanju =====
        Set<String> existingKeys = new HashSet<>();

        for (Row r : autoSorted) {
            existingKeys.add(key(r));
        }
        for (Row r : candidates) {
            existingKeys.add(key(r));
        }

        // ===== dodaj samo nove iz parsed.sorted =====
        for (Row r : parsed.sorted()) {
            if (existingKeys.add(key(r))) {
                autoSorted.add(r);
            }
        }

        // ===== dodaj samo nove iz parsed.candidates =====
        for (Row r : parsed.candidates()) {
            if (existingKeys.add(key(r))) {
                candidates.add(r);
            }
        }

        // ===== sortiraj ponovo =====
        autoSorted.sort(
                Comparator.comparing(Row::getLetter).thenComparing(Row::getArtist, String.CASE_INSENSITIVE_ORDER)
        );
    }

    private void export(Stage stage) {
        try {
            FileChooser fc = new FileChooser();
            fc.setInitialFileName("artist-sorter.xlsx");
            File file = fc.showSaveDialog(stage);
            if (file == null) {
                return;
            }

            exportService.export(autoSorted, candidates, file);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void importFile(Stage stage) {
        try {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(stage);
            if (file == null) {
                return;
            }

            ImportResult result = importService.importFile(file);

            autoSorted.setAll(result.sorted());
            candidates.setAll(result.candidates());
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }

    private TableView<Row> createAutoTable() {
        TableView<Row> table = new TableView<>();

        table.getColumns().add(col("Letter", Row::letterProperty, 60));
        table.getColumns().add(col("Artist", Row::artistProperty, 180));
        table.getColumns().add(col("Song", Row::songProperty, 200));
        table.getColumns().add(col("Type", Row::typeProperty, 120));

        // ===== return-to-candidates kolona =====
        TableColumn<Row, Void> returnCol = new TableColumn<>("↩");
        returnCol.setPrefWidth(60);

        returnCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("←");

            {
                btn.setOnAction(e -> {
                    Row row = getTableView().getItems().get(getIndex());

                    autoSorted.remove(row);
                    candidates.add(new Row("?", row.getArtist(), row.getSong(), ""));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().add(returnCol);

        return table;
    }

    private TableView<Row> createCandidateTable() {
        TableView<Row> table = new TableView<>();

        table.getColumns().add(col("Artist", Row::artistProperty, 180));
        table.getColumns().add(col("Song", Row::songProperty, 200));

        TableColumn<Row, String> typeCol = new TableColumn<>("Type");
        typeCol.setPrefWidth(260);

        typeCol.setCellFactory(col -> new CandidateTypeCell(autoSorted, table));

        table.getColumns().add(typeCol);

        return table;
    }

    private TableColumn<Row, String> col(
            String name,
            javafx.util.Callback<Row, SimpleStringProperty> prop,
            int width
    ) {
        TableColumn<Row, String> column = new TableColumn<>(name);
        column.setCellValueFactory(cellData -> prop.call(cellData.getValue()));
        column.setPrefWidth(width);
        return column;
    }

    private void clearAll() {
        autoSorted.clear();
        candidates.clear();
    }

    private String key(Row r) {
        return (r.getArtist() + "|" + r.getSong()).toLowerCase(Locale.ROOT);
    }

}
