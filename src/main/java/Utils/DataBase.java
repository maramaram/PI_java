package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DataBase {
    @FXML
    private Button checkconnection;

    @FXML
    void connectbutton(ActionEvent event) {
        DataBase dataBase = new DataBase() ;
        Connection connection = dataBase.getConnect();
    }
    public Connection connect;
    public Connection getConnect() {
        String DatabaseName = "pidev2";
        String username = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/" + DatabaseName + "?user=" + username + "&password=" + password;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection(url);
            System.out.println("Connected to the database");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return connect;
    }



}
