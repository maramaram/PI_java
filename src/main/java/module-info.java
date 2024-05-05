<<<<<<< HEAD
module com.example.pijava {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires okhttp3;
    requires com.google.gson;
    requires org.controlsfx.controls;

    requires com.google.protobuf;

    requires proto.google.cloud.speech.v1;
    requires com.google.auth.oauth2;
    requires assemblyai.java;


    opens com.example.pijava to javafx.fxml;
    opens Entities to javafx.base; // Ajout de cette ligne pour ouvrir le paquetage Entities à javafx.base
    opens Controller to javafx.fxml;
    exports com.example.pijava;

    exports Controller to javafx.fxml;


}
=======
module User {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires org.controlsfx.controls;
    requires org.apache.commons.codec;
    requires MaterialFX;
    requires VirtualizedFX;
    requires jakarta.mail;
    requires twilio;

    requires bcrypt;
    exports Controller;

    opens Controller to javafx.fxml;
    opens User;
    opens Utils;
    opens Entities to javafx.base;
}
>>>>>>> 23e81c01d9c8342b6b58c7bbe160fc8d8556d3ea
