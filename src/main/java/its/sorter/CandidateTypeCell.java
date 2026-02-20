package its.sorter;

import java.util.Comparator;
import java.util.Locale;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

class CandidateTypeCell extends TableCell<Row, String> {

    private final RadioButton bend = new RadioButton("Bend");
    private final RadioButton person = new RadioButton("Ime+Prezime");
    private final RadioButton pseudo = new RadioButton("Pseudonim");
    private final RadioButton duet = new RadioButton("Duet");

    private final ToggleGroup group = new ToggleGroup();
    private final HBox box = new HBox(5, bend, person, pseudo, duet);

    private final ObservableList<Row> autoSorted;
    private final TableView<Row> table;

    CandidateTypeCell(ObservableList<Row> autoSorted, TableView<Row> table) {
        this.autoSorted = autoSorted;
        this.table = table;

        bend.setToggleGroup(group);
        person.setToggleGroup(group);
        pseudo.setToggleGroup(group);
        duet.setToggleGroup(group);

        bend.setOnAction(e -> move("BEND"));
        person.setOnAction(e -> move("PERSON"));
        pseudo.setOnAction(e -> move("PSEUDONYM"));
        duet.setOnAction(e -> move("DUET"));
    }

    private void move(String type) {
        Row row = table.getItems().get(getIndex());

        String letter;

        if ("PERSON".equals(type)) {
            letter = row.getArtist()
                    .replaceAll(".*\\s", "")
                    .substring(0, 1)
                    .toUpperCase(Locale.ROOT);
        } else {
            // BEND, PSEUDONYM, DUET → prvo slovo
            letter = row.getArtist()
                    .substring(0, 1)
                    .toUpperCase(Locale.ROOT);
        }

        autoSorted.add(new Row(letter, row.getArtist(), row.getSong(), type));

        // ===== SORT: DUET NA KRAJ =====
        autoSorted.sort((a, b) -> {

            boolean aDuet = "DUET".equals(a.getType());
            boolean bDuet = "DUET".equals(b.getType());

            if (aDuet && !bDuet) return 1;
            if (!aDuet && bDuet) return -1;

            if (aDuet) {
                return a.getArtist().compareToIgnoreCase(b.getArtist());
            }

            int letterCmp = a.getLetter().compareToIgnoreCase(b.getLetter());
            if (letterCmp != 0) return letterCmp;

            return a.getArtist().compareToIgnoreCase(b.getArtist());
        });

        table.getItems().remove(row);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
            group.selectToggle(null);
            return;
        }

        Row row = getTableView().getItems().get(getIndex());

        switch (row.getType()) {
            case "BEND" -> group.selectToggle(bend);
            case "PERSON" -> group.selectToggle(person);
            case "PSEUDONYM" -> group.selectToggle(pseudo);
            case "DUET" -> group.selectToggle(duet);
            default -> group.selectToggle(null);
        }

        setGraphic(box);
    }
}
