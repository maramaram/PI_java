package Utils;

import java.sql.*;
import java.time.LocalDate;

import Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public static Connection connect;
    public static Connection getConnect() {
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
    public static ObservableList<User> getDatauser(){
        Connection conn = getConnect();
        ObservableList<User> list = FXCollections.observableArrayList();
        try {
            conn = getConnect();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
               String id = rs.getString("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String tel = rs.getString("num_tel");
                String role = rs.getString("role");
                String adress = rs.getString("adress");
                String photo = rs.getString("photo");
                LocalDate dn = rs.getDate("date_N").toLocalDate();
                String status = rs.getString("status");

                list.add(new User(id,nom,prenom,email, dn,role,adress,photo,status,tel));
                System.out.println("ffff");
            }  } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}
