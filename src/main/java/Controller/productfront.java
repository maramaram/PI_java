package Controller;

import Entities.product;
import Service.productService;
import com.example.projectpi.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class productfront extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/products/basefront.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    @FXML
    private HBox card;
    public void addCard(product product) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Products/card.fxml"));
            HBox cardBox = fxmlLoader.load();
            cardController cardproduit = fxmlLoader.getController();
            if (cardproduit != null) {
                cardproduit.setData(product);
                card.getChildren().add(cardBox);
            } else {
                System.out.println("cardproduit is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        // La méthode initialize sera appelée après que les éléments FXML annotés ont été injectés
        // Donc, vous pouvez accéder en toute sécurité à this.card ici
        try {
            // Récupérer les produits depuis la base de données
            productService service = new productService();
            List<product> productList = service.afficherListFront();

            // Ajouter une carte pour chaque produit à cardContainer
            for (product product : productList) {
                addCard(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

