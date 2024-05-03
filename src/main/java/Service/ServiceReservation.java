package Service;

import Entities.Reservation;
import Entities.Session;
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
        statement.setDate(2, Date.valueOf(reservation.getDate())); // Convert Date to java.sql.Date
        statement.setString(3, reservation.getEtat());
        statement.setString(4, reservation.getClient());
        statement.executeUpdate();
    }
    @Override
    public void modifier(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservation SET session = ?, date = ?, etat = ? , Client = ? WHERE id = ?";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);

        preparedStatement.setString(1, reservation.getSession());
        LocalDate date = LocalDate.parse(reservation.getDate());
        preparedStatement.setDate(2, java.sql.Date.valueOf(date));
        preparedStatement.setString(3, reservation.getEtat());
        preparedStatement.setString(4, reservation.getClient());
        preparedStatement.setInt(5, reservation.getId());
        preparedStatement.executeUpdate();
        System.out.println("Reservation modifier");
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
        List<Reservation> re = new ArrayList<>();
        String sql = "SELECT * FROM reservation";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            Reservation r = new Reservation();

            re.add(new Reservation(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4) ,  rs.getDate(5)));
        }
        return re;
    }
}
