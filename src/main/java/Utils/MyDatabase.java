package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String URL ="jdbc:mysql://localhost:3306/pidev2";
    private final String USERNAME ="root";
    private final String PWD ="";
    public static Connection connect;
    private Connection conx;

    public static MyDatabase instance;

    private MyDatabase(){
        try {
            conx = DriverManager.getConnection(URL,USERNAME,PWD);
            System.out.println("Connexion établie!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance(){
        if (instance == null){
            instance = new MyDatabase();
        }
        return instance;

    }

    public Connection getConnection(){return conx;}
    public Connection getConx() {
        return conx;
    }
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
}

