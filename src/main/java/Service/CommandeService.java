package Service;

import Entities.Commande;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements ICRUD<Commande> {

    private Connection conx;
    private PreparedStatement pstm;

    public CommandeService() {
        conx = MyDatabase.getInstance().getConx();
    }

    @Override
    public void add(Commande commande) throws SQLException {
        String req = "INSERT INTO `commande`(`livreur_id`, `user_id`, `statut`, `prixtotale`) VALUES (?, ?, ?, ?)";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, commande.getLivreurId());
        pstm.setInt(2, commande.getUserId());
        pstm.setString(3, commande.getStatut());
        pstm.setInt(4, commande.getPrixTotal());

        pstm.executeUpdate();
        System.out.println("Commande ajoutée");
    }

    @Override
    public void addMeth2(Commande commande) throws SQLException {

    }

    @Override
    public void modifier(Commande commande) throws SQLException {
        String req = "UPDATE `commande` SET `livreur_id` = ?, `user_id` = ?, `statut` = ?, `prixtotale` = ? WHERE `id` = ?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, commande.getLivreurId());
        pstm.setInt(2, commande.getUserId());
        pstm.setString(3, commande.getStatut());
        pstm.setInt(4, commande.getPrixTotal());
        pstm.setInt(5, commande.getId());

        int rowsAffected = pstm.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Commande modifiée");
        } else {
            System.out.println("Aucune commande n'a été modifiée");
        }
    }

    @Override
    public void delete(Commande commande) throws SQLException {
        String req = "DELETE FROM `commande` WHERE `id` = ?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, commande.getId());

        int rowsAffected = pstm.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Commande supprimée");
        } else {
            System.out.println("Aucune commande n'a été supprimée");
        }
    }

    @Override
    public List<Commande> afficherList() throws SQLException {
        String req = "SELECT * FROM `commande`";

        Statement stm = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Commande> commandes = new ArrayList<>();

        while (res.next()) {
            commandes.add(new Commande(
                    res.getInt("id"),
                    res.getInt("livreur_id"),
                    res.getInt("user_id"),
                    res.getString("statut"),
                    res.getInt("prixtotale")
            ));
        }

        return commandes;
    }

    @Override
    public List<Commande> afficherListSearch(String s) throws SQLException {
        return null;
    }
}
