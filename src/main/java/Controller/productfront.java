package Controller;

import Entities.SessionManager;
import Entities.product;
import Service.productService;
import com.example.projectpi.HelloApplication;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class productfront  {
    String userId = SessionManager.getInstance().getUserId();
    //@FXML
    //private VBox cardContainer;
    //private HBox currentRow = null;
    //private int productCount = 0;
    @FXML
    private TilePane cardContainer;
    @FXML
    private TextField searchField;
    private panierfront panierfront; // Déplacer la déclaration ici
    private List<product> productList; // Déclarer productList en tant que champ de classe

    @FXML
    void cartview(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paniers/basefrontp.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void navigatechatbot(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/products/openai.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void initialize() {
        try {
            // Charger les produits depuis la base de données
            productService service = new productService();
            productList = service.afficherListFront(); // Assigner la liste de produits à productList

            // Ajouter une carte pour chaque produit à cardContainer
            for (product product : productList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Products/card.fxml"));
                HBox cardBox = fxmlLoader.load();
                cardController cardproduit = fxmlLoader.getController();

                if (cardproduit != null) {
                    cardproduit.setData(product);
                    cardproduit.setpanierfront(panierfront); // Passer une référence au contrôleur de panier

                    // Créer un Border avec un style de votre choix
                    Border border = new Border(new BorderStroke(
                            Color.BLACK, // Couleur du bord
                            BorderStrokeStyle.SOLID, // Style du bord
                            CornerRadii.EMPTY,
                            BorderWidths.DEFAULT)
                    );

                    // Appliquer le Border à la carte
                    cardBox.setBorder(border);

                    cardContainer.getChildren().add(cardBox); // Ajouter la carte à cardContainer
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }



    public void addCard(product product) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Products/card.fxml"));
            HBox cardBox = fxmlLoader.load();
            cardController cardproduit = fxmlLoader.getController();

            if (cardproduit != null) {
                cardproduit.setData(product);

                // Créer un Border
                Border border = new Border(new BorderStroke(
                        Color.BLACK,
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        BorderWidths.DEFAULT)
                );

                cardBox.setBorder(border);

                cardContainer.getChildren().add(cardBox);
            } else {
                System.out.println("cardproduit is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleSearch(KeyEvent event) {
        String searchTerm = searchField.getText().toLowerCase(); // Convertir la recherche en minuscules pour la correspondance insensible à la casse
        if (searchTerm.isEmpty()) {
            // Si le champ de recherche est vide, affichez tous les produits
            // Remplacer cartProducts par votre liste de produits
            displayProducts(productList);
        } else {
            // Filtrer les produits pour afficher uniquement ceux correspondant au terme de recherche
            List<product> filteredProducts = productList.stream()
                    .filter(product -> product.getNom().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
            // Mettre à jour l'affichage avec les produits filtrés
            displayProducts(filteredProducts);
        }
    }

    private void displayProducts(List<product> products) {
        cardContainer.getChildren().clear(); // Effacer les anciens produits
        // Ajouter les produits filtrés ou non filtrés
        for (product product : products) {
            addCard(product);
        }
    }
}