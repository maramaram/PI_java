module com.example.pijava {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.pijava to javafx.fxml;
    opens Entities to javafx.base; // Ajout de cette ligne pour ouvrir le paquetage Entities à javafx.base
    opens Controller to javafx.fxml;

    exports com.example.pijava;
    exports Controller to javafx.fxml;

}
