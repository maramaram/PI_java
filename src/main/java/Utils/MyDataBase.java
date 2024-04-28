package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;

public class MyDataBase {
    static String user = "root";
    static String password = "";
    static String url = "jdbc:mysql://localhost:3306/session2";
    static String driver = "com.mysql.cj.jdbc.Driver";
    public static MyDataBase instance;
    private Connection connection;

    // Private constructor to prevent instantiation from outside
    private MyDataBase() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion établie !");
        } catch ( SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static MyDataBase getInstance(){
        if(instance == null)
            instance = new MyDataBase();
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
}
