module its.sorter {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens its.sorter to javafx.fxml;
    exports its.sorter;
}
