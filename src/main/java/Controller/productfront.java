package Controller;

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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class productfront extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/products/basefront.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
    //@FXML
    //private VBox cardContainer;
    //private HBox currentRow = null;
    //private int productCount = 0;
    @FXML
    private TilePane cardContainer;
    
    private panierfront panierfront; // Déplacer la déclaration ici
    @FXML
    void viewcart(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paniers/basefrontp.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur si nécessaire
            // basefrontpController controller = loader.getController();

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
            List<product> productList = service.afficherListFront();

            // Ajouter une carte pour chaque produit à cardContainer
            for (product product : productList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Products/card.fxml"));
                HBox cardBox = fxmlLoader.load();
                cardController cardproduit = fxmlLoader.getController();

                if (cardproduit != null) {
                    cardproduit.setData(product);
                    cardproduit.setpanierfront(panierfront); // Passer une référence au contrôleur de panier
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
    }}