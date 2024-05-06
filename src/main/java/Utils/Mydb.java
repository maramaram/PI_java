package Utils;
import java.sql.*;


public class Mydb {
    final String url="jdbc:mysql://localhost:3306/gestion produits";
    final String user="root";
    final String pass="";
    private Connection connection;
    private static Mydb instance;
    private Mydb(){
        try{
            connection= DriverManager.getConnection(url,user,pass);
            System.out.println("Connected");
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
    public static Mydb getInstance(){
        if(instance==null)
            instance=new Mydb();
        return instance;
    }
    public Connection getConnection(){return connection;}
}
