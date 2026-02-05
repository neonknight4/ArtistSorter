/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package its.sorter;

import java.util.Comparator;
import java.util.Locale;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 *
 * @author mihailo-jankovic
 */
class CandidateTypeCell extends TableCell<Row, String> {

    private final RadioButton bend = new RadioButton("Bend");
    private final RadioButton person = new RadioButton("Ime+Prezime");
    private final RadioButton pseudo = new RadioButton("Pseudonim");
    private final ToggleGroup group = new ToggleGroup();
    private final HBox box = new HBox(5, bend, person, pseudo);

    private final ObservableList<Row> autoSorted;
    private final TableView<Row> table;

    CandidateTypeCell(ObservableList<Row> autoSorted, TableView<Row> table) {
        this.autoSorted = autoSorted;
        this.table = table;

        bend.setToggleGroup(group);
        person.setToggleGroup(group);
        pseudo.setToggleGroup(group);

        bend.setOnAction(e -> move("BEND"));
        person.setOnAction(e -> move("PERSON"));
        pseudo.setOnAction(e -> move("PSEUDONYM"));
    }

    private void move(String type) {
        Row row = table.getItems().get(getIndex());

        String letter = type.equals("PERSON")
                ? row.getArtist().replaceAll(".*\\s", "").substring(0, 1).toUpperCase(Locale.ROOT)
                : row.getArtist().substring(0, 1).toUpperCase(Locale.ROOT);

        autoSorted.add(new Row(letter, row.getArtist(), row.getSong(), type));
        autoSorted.sort(Comparator.comparing(Row::getLetter)
                .thenComparing(Row::getArtist, String.CASE_INSENSITIVE_ORDER));

        table.getItems().remove(row);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
            group.selectToggle(null); // ← KLJUČNO: reset selekcije
            return;
        }

        Row row = getTableView().getItems().get(getIndex());

        // sinhronizuj UI sa modelom
        switch (row.getType()) {
            case "BEND" ->
                group.selectToggle(bend);
            case "PERSON" ->
                group.selectToggle(person);
            case "PSEUDONYM" ->
                group.selectToggle(pseudo);
            default ->
                group.selectToggle(null);
        }

        setGraphic(box);
    }
}
