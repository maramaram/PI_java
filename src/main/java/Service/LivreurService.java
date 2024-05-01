package Service;

import Entities.Livreur;
import Utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivreurService implements ICRUD<Livreur> {

    private Connection conx;
    private PreparedStatement pstm;

    public LivreurService() {
        conx = MyDatabase.getInstance().getConx();
    }

    @Override
    public void add(Livreur livreur) throws SQLException {
        String req = "INSERT INTO `livreur`(`nom`, `prenom`, `disponibilite`, `image`, `note`) VALUES (?, ?, ?, ?, ?)";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, livreur.getNom());
        pstm.setString(2, livreur.getPrenom());
        pstm.setString(3, livreur.getDisponibilite());
        pstm.setString(4, livreur.getImage());
        pstm.setInt(5, livreur.getNote());

        pstm.executeUpdate();
        System.out.println("Livreur ajouté");
    }

    @Override
    public void addMeth2(Livreur livreur) throws SQLException {

    }

    @Override
    public void modifier(Livreur livreur) throws SQLException {
        String req = "UPDATE `livreur` SET `nom` = ?, `prenom` = ?, `disponibilite` = ?, `image` = ?, `note` = ? WHERE `id` = ?";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, livreur.getNom());
        pstm.setString(2, livreur.getPrenom());
        pstm.setString(3, livreur.getDisponibilite());
        pstm.setString(4, livreur.getImage());
        pstm.setInt(5, livreur.getNote());
        pstm.setInt(6, livreur.getId());

        int rowsAffected = pstm.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Livreur modifié");
        } else {
            System.out.println("Aucun livreur n'a été modifié");
        }
    }

    @Override
    public void delete(Livreur livreur) throws SQLException {
        String req = "DELETE FROM `livreur` WHERE `id` = ?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, livreur.getId());

        int rowsAffected = pstm.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Livreur supprimé");
        } else {
            System.out.println("Aucun livreur n'a été supprimé");
        }
    }

    @Override
    public List<Livreur> afficherList() throws SQLException {
        String req = "SELECT * FROM `livreur`";

        PreparedStatement stm = conx.prepareStatement(req);
        ResultSet res = stm.executeQuery();

        List<Livreur> livreurs = new ArrayList<>();

        while (res.next()) {
            livreurs.add(new Livreur(res.getInt("id"),
                    res.getString("nom"),
                    res.getString("prenom"),
                    res.getString("disponibilite"),
                    res.getString("image"),
                    res.getInt("note")));
        }

        return livreurs;
    }

    @Override
    public List<Livreur> afficherListSearch(String s) throws SQLException {
        String req = "SELECT * FROM livreur WHERE nom LIKE '"+"%"+s+"%"+"' OR prenom LIKE '"+"%"+s+"%'";

        PreparedStatement stm = conx.prepareStatement(req);
        ResultSet res = stm.executeQuery();

        List<Livreur> livreurs = new ArrayList<>();

        while (res.next()) {
            livreurs.add(new Livreur(res.getInt("id"),
                    res.getString("nom"),
                    res.getString("prenom"),
                    res.getString("disponibilite"),
                    res.getString("image"),
                    res.getInt("note")));
        }

        return livreurs;
    }

    public Livreur getLivreurById(int livreurId) throws SQLException {
        String req = "SELECT * FROM `livreur` WHERE `id` = ?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, livreurId);

        ResultSet res = pstm.executeQuery();

        Livreur livreur = null;

        if (res.next()) {
            livreur = new Livreur(res.getInt("id"),
                    res.getString("nom"),
                    res.getString("prenom"),
                    res.getString("disponibilite"),
                    res.getString("image"),
                    res.getInt("note"));
        }

        return livreur;
    }


}
