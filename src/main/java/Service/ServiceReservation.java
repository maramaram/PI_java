package Service;

import Entities.Reservation;
import Utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IService<Reservation> {

    private Connection connection;

    public ServiceReservation(){
        connection = MyDataBase.getInstance().getConnection();
    }
Date date;
    @Override
    public void add(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO `reservation`(`session`, `date`, `etat`, `client`) VALUES (?, ?, ?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, reservation.getSession());
        statement.setDate(2, new java.sql.Date(date.getTime())); // Convert Date to java.sql.Date
        statement.setString(3, reservation.getEtat());
        statement.setString(4, reservation.getEtat());
        statement.executeUpdate();
    }

    @Override
    public void modifier(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservation SET session_id = ?, date = ?, etat = ? WHERE id = ?";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setString(1, reservation.getSession());
        preparedStatement.setDate(3, java.sql.Date.valueOf(reservation.getDate()));
        preparedStatement.setString(3, reservation.getEtat());
        preparedStatement.setInt(4, reservation.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Reservation reservation) throws SQLException {
        String sql= "DELETE FROM reservation WHERE id = ?";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setInt(1, reservation.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Reservation> afficherList() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            Reservation r = new Reservation();
            r.setId(rs.getInt("id"));
            r.setSession(rs.getString("session"));
            r.setDate(Date.valueOf(String.valueOf(LocalDate.parse(String.valueOf(rs.getDate("Date"))))));
            r.setEtat(rs.getString("etat"));
            reservations.add(r);
        }
        return reservations;
    }
}
