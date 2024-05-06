
package Service;

import Entities.*;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class productService implements ICRUDS<product> {

    private static Connection connection= MyDatabase.getInstance().getConnection();;



    @Override
    public void ajouterproduit(product product) throws SQLException {

        String sql = "INSERT INTO produits (nom, description, quantite_stock, categories, prix, image) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getNom());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setInt(3, product.getQuantite_stock());
            preparedStatement.setString(4, product.getCategories());
            preparedStatement.setFloat(5, product.getPrix());
            preparedStatement.setString(6, product.getImage());


            preparedStatement.executeUpdate();
            System.out.println("produit ajouté");
        }
    }

    @Override
    public void updateProduit(int id, product product) throws SQLException {
        String sql = "UPDATE produits SET nom = ?, description = ?, quantite_stock = ?, categories = ?, prix = ?, image = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getNom());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setInt(3, product.getQuantite_stock());
            preparedStatement.setString(4, product.getCategories());
            preparedStatement.setFloat(5, product.getPrix());
            preparedStatement.setString(6, product.getImage());
            preparedStatement.setInt(7, id); // Spécifiez l'ID du produit à mettre à jour
            preparedStatement.executeUpdate();
            System.out.println("Produit mis à jour avec succès !");
        }
    }
    @Override
    public void deleteproduit(int id) throws SQLException {

        String sql = "DELETE FROM produits WHERE id = ?" ;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<product> afficherList() throws SQLException {
        String req = "SELECT * FROM produits";
        List<product> products = new ArrayList<>();

        try (Statement stm = connection.createStatement();
             ResultSet res = stm.executeQuery(req)) {
            while (res.next()) {
                int id = res.getInt("id");
                String nom = res.getString("nom");
                String description = res.getString("description");
                int quantite_stock = res.getInt("quantite_stock");
                String categories = res.getString("categories");
                float prix = res.getFloat("prix");
                String image = res.getString("image");

                product p = new product(id, nom, description, quantite_stock, categories, prix, image);
                products.add(p);
            }
        }

        return products;
    }

    public product getProduitByNom(String nom) throws SQLException {
        String sql = "SELECT * FROM produits WHERE nom = ?";
        product produit = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nom);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String description = resultSet.getString("description");
                    int quantite_stock = resultSet.getInt("quantite_stock");
                    String categories = resultSet.getString("categories");
                    float prix = resultSet.getFloat("prix");
                    String image = resultSet.getString("image");

                    produit = new product(id, nom, description, quantite_stock, categories, prix, image);
                }
            }
        }

        return produit;
    }
    @Override
    public List<product> afficherListFront() throws SQLException {
        String req = "SELECT * FROM produits";
        List<product> productList= new ArrayList<>(); // Utilisez un nom de variable en minuscules pour la liste

        try (Statement stm = connection.createStatement();
             ResultSet res = stm.executeQuery(req)) {
            while (res.next()) {
                int id = res.getInt("id");
                String nom = res.getString("nom");
                String description = res.getString("description");
                int prix = res.getInt("prix");
                int quantite_stock= res.getInt("quantite_stock");
                String categories = res.getString("categories");
                String image = res.getString("image");

                product pr = new product(id, nom, description,quantite_stock, categories,  prix,  image);

                // Ajoutez chaque produit à la liste
                productList.add(pr);
            }
        }

        return productList; // Retournez la liste de produits
    }
}
