package Service;

import Entities.Session;
import Utils.MyDataBase;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;



public class ServiceSession implements IService<Session> {

    private Connection conx;
    private Statement stm;
    private PreparedStatement pstm;

    public ServiceSession() {
        conx = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void add(Session session) throws SQLException {
        String req = "INSERT INTO `session` (`cap`, `type`, `date`, `coach`) VALUES ('" + session.getCap() + "','" + session.getType() + "','" + session.getDate() + "','" + session.getCoach() + "')";
        stm = conx.createStatement();
        stm.executeUpdate(req);
        System.out.println("Session ajoutée avec succes");
    }

    @Override
    public void modifier(Session session) throws SQLException {
        String req = "Update session set cap = ?, type= ? , date= ? , coach= ? where id = ?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, (session.getCap()));
        pstm.setString(2, session.getType());
        LocalDate date = LocalDate.parse(session.getDate()); // Assuming 'date' is a String in the format 'yyyy-MM-dd'
        pstm.setDate(3, java.sql.Date.valueOf(date));
        pstm.setString(4, session.getCoach());
        pstm.setInt(5, session.getId());
        pstm.executeUpdate();
        System.out.println("Session modifier");
    }

    @Override
    public void delete(Session session) throws SQLException {

        String req = "delete from session where id = ?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, session.getId());
        pstm.executeUpdate();
        System.out.println("Session supprimer");
    }

    @Override
    public List<Session> afficherList() throws SQLException {
        String req = "SELECT * FROM `session`";

        stm = conx.createStatement();
        ResultSet res = stm.executeQuery(req);
        List<Session> se = new ArrayList<>();

        while (res.next()) {
            se.add(new Session(res.getInt(1), res.getInt(2), res.getString(3), res.getDate(4), res.getString(5)));
        }
        return se;
    }

    public List<Session> afficherListSearch(String s) throws SQLException {
        String req = "SELECT * FROM `session` WHERE `type` LIKE '" + "%" + s + "%" + "' OR `coach` LIKE '" + "%" + s + "%'";

        stm = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Session> se = new ArrayList<>();

        while (res.next()) {
            se.add(new Session(res.getInt(1), res.getInt(2), res.getString(3), res.getDate(4), res.getString(5)));
        }
        return se;
    }
}