module tn.esprit.javafxproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;


    opens tn.esprit.javafxproject to javafx.fxml;
    exports tn.esprit.javafxproject;
}