package Service;

import Entities.Session;
import Utils.MyDataBase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceSession implements IService<Session> {

    private Connection connection;

    public ServiceSession(){
        connection= MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Session session) throws SQLException {
        String dateString = String.valueOf(session.getDate()); // Example date string
        if(dateString == null || dateString.isEmpty()) {
            System.out.println(" error");
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.err.println("Error parsing date : " + e+ e.getMessage());
            // Handle the parse exception
            e.printStackTrace();
            // You may choose to throw the exception or return from the method here
            return;
        }

        String sql = "INSERT INTO `session`(`cap`, `des`, `Type`, `Date`, `Coach`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, session.getCap());
        statement.setString(2, session.getDes());
        statement.setString(3, session.getType());
        statement.setDate(4, new java.sql.Date(date.getTime())); // Convert Date to java.sql.Date
        statement.setString(5, session.getCoach());
        System.out.println("eeeeeeeeeeeeeee");
        statement.executeUpdate();
    }

    @Override
    public void modifier(Session session) throws SQLException {
        String sql = "Update session set cap = ?, des= ? , type= ? , Date= ? , Coach= ? where id = ?";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(session.getCap()));
        preparedStatement.setString(2, session.getDes());
        preparedStatement.setString(3,session.getType());
        preparedStatement.setDate(4, java.sql.Date.valueOf(session.getDate()));
        preparedStatement.setString(5,session.getCoach());
        preparedStatement.executeUpdate();
    }

    @Override
    public void supprimer(Session session) throws SQLException {

        String sql= "delete from session where id = ?";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setInt(1,session.getId());
        preparedStatement.executeUpdate();
    }
    @Override
    public List<Session> afficher() throws SQLException {
        List<Session> sessions= new ArrayList<>();
        String sql = "select * from session";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            Session s = new Session();
            s.setId(rs.getInt("id"));
            s.setCap(Integer.valueOf(rs.getString("cap")));
            s.setDes(rs.getString("des"));
            s.setType(rs.getString("type"));
            s.setDate(String.valueOf(LocalDate.parse(String.valueOf(rs.getDate("Date")))));
            s.setCoach(rs.getString("coach"));
            sessions.add(s);
        }
        return sessions;
    }
}