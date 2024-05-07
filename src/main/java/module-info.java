module com.example.pijava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    //requires mysql.connector.java; // Check the correct module name
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
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    opens com.example.pijava to javafx.fxml;
    opens Controller to javafx.fxml;
    opens Utils;
    opens Entities to javafx.base;
    opens User to javafx.graphics;

    exports com.example.projectpi;
    exports Controller;
}
