module com.example.projectpi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    exports Controller;

    opens Controller to javafx.fxml;
    opens com.example.projectpi to javafx.fxml;
    opens Entities to javafx.base;

    exports com.example.projectpi;
}