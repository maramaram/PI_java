package Service;
import Entities.*;
import Utils.Mydb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class panierService  implements PCRUD<panier>{
    private static Connection connection;
    public panierService() throws SQLException {


        connection= Mydb.getInstance().getConnection();

    }
    @Override
    public void ajouterpanier(panier panier) throws SQLException {

        String sql = "INSERT INTO panier (prix_tot, quantite) VALUES (?, ?)";
        ;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, panier.getPrix_tot());
            preparedStatement.setInt(2, panier.getQuantite());



            preparedStatement.executeUpdate();
           /* ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int panierId = generatedKeys.getInt(1);
                for (product produit : product.getproduits()) {
                    ajouterProduitAuPanier(panierId, produit.getId());
                }
            }*/
            System.out.println("panier ajouté");
        }
    }


    @Override
    public void updatepanier(int id, panier panier) throws SQLException {
        String sql = "UPDATE panier SET prix_tot = ?, quantite = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, panier.getPrix_tot());
            preparedStatement.setInt(2, panier.getQuantite());
            preparedStatement.setInt(3, id); // Spécifiez l'ID du produit à mettre à jour
            preparedStatement.executeUpdate();
            System.out.println("Panier mis à jour avec succès !");
        }
    }
    @Override
    public void deletepanier(int id) throws SQLException {

        String sql = "DELETE FROM panier WHERE id = ?" ;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<panier> afficherp() throws SQLException {
        String req = "SELECT * FROM panier";
        List<panier> paniers = new ArrayList<>();

        try (Statement stm = connection.createStatement();
             ResultSet res = stm.executeQuery(req)) {
            while (res.next()) {
                int id = res.getInt("id");
                int prix_tot = res.getInt("prix_tot");
                int quantite = res.getInt("quantite");


                panier p = new panier(id, prix_tot, quantite);
                paniers.add(p);
            }
        }

        return paniers;
    }
    public int creerPanier() throws SQLException {
        String sql = "INSERT INTO panier (prix_tot, quantite) VALUES (?, ?)";
        int panierId = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setFloat(1, 0); // Prix total initialisé à 0
            preparedStatement.setInt(2, 0); // Quantité initialisée à 0
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    panierId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("La création du panier a échoué, aucun ID retourné.");
                }
            }
        }

        return panierId;
    }
    public List<product> getProduitsDuPanier(int panierId) throws SQLException {
        String sql = "SELECT p.* FROM produits p INNER JOIN panier_produits pp ON p.id = pp.produit_id WHERE pp.panier_id = ?";
        List<product> produits = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, panierId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nom = resultSet.getString("nom");
                    String description = resultSet.getString("description");
                    int quantite_stock = resultSet.getInt("quantite_stock");
                    String categories = resultSet.getString("categorie");
                    float prix = resultSet.getFloat("prix");
                    String image = resultSet.getString("image");

                    product produit = new product(id, nom, description, quantite_stock, categories, prix, image);
                    produits.add(produit);
                }
            }
        }

        return produits;
    }

    @Override
    public void ajouterProduitAuPanier(int panierId, int produitsId) throws SQLException {
        String sql = "INSERT INTO panier_produits (panier_id, produits_id) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, panierId);
            preparedStatement.setInt(2, produitsId);

            preparedStatement.executeUpdate();
        }
    }


}