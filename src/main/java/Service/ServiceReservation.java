package Service;

import Entities.Reservation;
import Entities.Session;
import Utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IService<Reservation> {

    private Connection conx;
    private Statement stm;
    private PreparedStatement pstm;

    public ServiceReservation()
    {
        conx = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void add(Reservation reservation) throws SQLException {
        String req = "INSERT INTO `reservation`(`session`, `date`, `etat`, `client`) VALUES ('" + reservation.getSession() + "' ,'" + reservation.getDate() + "', '" + reservation.getEtat() + "', '" + reservation.getClient() + "')";
stm = conx.createStatement();
stm.executeUpdate(req);
System.out.println("Reservation ajoutée avec succés");
    }

    @Override
    public void modifier(Reservation reservation) throws SQLException {
        String req = "UPDATE `reservation` SET session = ?, date = ?, etat = ? , client = ? WHERE id = ?";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, reservation.getSession());
        LocalDate date = LocalDate.parse(reservation.getDate());
        pstm.setDate(2, java.sql.Date.valueOf(date));
        pstm.setString(3, reservation.getEtat());
        pstm.setString(4, reservation.getClient());
        pstm.setInt(5, reservation.getId());
        pstm.executeUpdate();
        System.out.println("Reservation modifiée avec succes");
    }

    @Override
    public void delete(Reservation reservation) throws SQLException {
        String req= "DELETE FROM reservation WHERE id = ?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, reservation.getId());
        pstm.executeUpdate();
        System.out.println("Reservation suprimée avec succes");
    }

    @Override
    public List<Reservation> afficherList() throws SQLException {
        List<Reservation> re = new ArrayList<>();
        String req = "SELECT * FROM reservation";
        stm  = conx.createStatement();
        ResultSet rs = stm.executeQuery(req);
        while (rs.next()){
            Reservation r = new Reservation();
            re.add(new Reservation(rs.getInt(1), rs.getString(2), rs.getDate(3),rs.getString(4) , rs.getString(5)));
        }
        return re;
    }
}