module org.kih.rrsimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.kih.rrsimulator to javafx.fxml;
    exports org.kih.rrsimulator;
}