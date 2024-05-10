package Service;

import Entities.Commande;
import Utils.MyDatabase;

import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;


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
        // Supprimer les lignes de commandeproduit correspondant à la commande
        String reqCommandeProduit = "DELETE FROM commandeproduit WHERE commande_id = ?";
        try (PreparedStatement pstmCommandeProduit = conx.prepareStatement(reqCommandeProduit)) {
            pstmCommandeProduit.setInt(1, commande.getId());
            int rowsAffectedCommandeProduit = pstmCommandeProduit.executeUpdate();
            System.out.println(rowsAffectedCommandeProduit + " lignes de commandeproduit ont été supprimées");
        }


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


    public List<Commande> afficherListwithuserid(String iduser) throws SQLException {
        String req = "SELECT * FROM `commande` WHERE user_id = ?";

        PreparedStatement pstm = conx.prepareStatement(req);
        pstm.setString(1, iduser);
        ResultSet res = pstm.executeQuery();

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
        String req = "SELECT * FROM commande WHERE statut LIKE '"+"%"+s+"%"+"' ";

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

    public void addcommandeproduit(int idcommande, int idpanier) throws SQLException {
        // Récupérer les produits_id correspondants au panier_id donné
        String selectQuery = "SELECT produits_id FROM panier_produits WHERE panier_id = ?";
        try (PreparedStatement selectStmt = conx.prepareStatement(selectQuery)) {
            selectStmt.setInt(1, idpanier);
            ResultSet rs = selectStmt.executeQuery();

            // Insérer les produits_id dans la table commandeproduit
            String insertQuery = "INSERT INTO commandeproduit (commande_id, produits_id) VALUES (?, ?)";
            while (rs.next()) {
                int idproduit = rs.getInt("produits_id");
                try (PreparedStatement insertStmt = conx.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, idcommande);
                    insertStmt.setInt(2, idproduit);
                    insertStmt.executeUpdate();
                }
            }

            System.out.println("Les produits du panier ont été ajoutés à la commande.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout des produits du panier à la commande : " + e.getMessage());
        }
    }


/*
    public void addcommandeproduit(int idcommande , int idproduit) throws SQLException {
        String req = "INSERT INTO `commandeproduit`(`commande_id`, `produits_id`) VALUES (?, ?, ?, ?)";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, idcommande);
        pstm.setInt(2, idproduit);
        pstm.executeUpdate();
        System.out.println("Commandeproduit ajoutée");
    }
    public List<Map.Entry<Integer, Integer>> afficherListcommandeproduit() throws SQLException {
        String req = "SELECT * FROM `commandeproduit`";

        // Assurez-vous que 'conx' est initialisé correctement

        Statement stm = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Map.Entry<Integer, Integer>> liste = new ArrayList<>();

        while (res.next()) {
            int commandeId = res.getInt("commande_id");
            int produitId = res.getInt("produits_id");
            liste.add(new SimpleEntry<>(commandeId, produitId));
        }
        return liste;
    } */
}
