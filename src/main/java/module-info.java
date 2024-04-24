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

    exports Controller;

    opens Controller to javafx.fxml;
    opens User;
    opens Utils;
    opens Entities to javafx.base;
}