module tn.esprit.javafxproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;


    opens Controller to javafx.fxml; // Ouvrir l'accès au package Controller
    opens tn.esprit.javafxproject to javafx.fxml;
    exports tn.esprit.javafxproject;
    exports Entities;
    opens Entities to javafx.fxml;
    exports Utils;
    opens Utils to javafx.fxml;
    exports Controller;
}