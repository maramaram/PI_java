package Utils;
import Service.panierService;
import Entities.panier;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) throws SQLException {
        try { // Création d'une instance de productService
            panierService ps = new panierService();

            // Suppression du produit avec l'ID spécifié
            int idToDelete = 32; // Vous pouvez changer cet ID si nécessaire
            ps.deletepanier(idToDelete);

            System.out.println("Produit avec l'ID " + idToDelete + " supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Une erreur s'est produite lors de la suppression du produit : " + e.getMessage());
        }


    }  }