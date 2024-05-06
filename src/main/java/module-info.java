module com.example.projectpi {
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

    requires org.apache.pdfbox;
    requires java.desktop;
    requires okhttp3;
    requires com.google.gson;
    requires com.google.protobuf;
    requires proto.google.cloud.speech.v1;
    requires com.google.auth.oauth2;
    requires assemblyai.java;
    requires org.apache.poi.ooxml;
    requires stripe.java;

    // Add any additional module dependencies here

    opens com.example.pijava to javafx.fxml;
    opens Controller to javafx.fxml;
    opens User;
    opens Utils;
    opens Entities to javafx.base;

    // Ensure that all opens and exports are consistent with your project structure

    exports com.example.projectpi;
    exports Controller;
}
