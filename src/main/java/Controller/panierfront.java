package Controller;
import Entities.panier;
import Entities.product;
import Service.panierService;
import Service.productService;
import com.example.projectpi.HelloApplication;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class panierfront extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/paniers/basefrontp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @FXML
    public ListView<product> cartListView;

    private ObservableList<product> cartProducts = FXCollections.observableArrayList();
    @FXML
    private Spinner<?> spinner;
    @FXML
    private TextField pricetot;
    @FXML
    private Button remove;
    @FXML
    private Button valider;
    @FXML
    private Label shop;
    @FXML
    void viewshop(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/products/basefront.fxml"));
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
    public void initialize() {
        try {
            panierService ez = new panierService();
            List<product> productList = ez.afficherList().getproductList();

            // Clear the existing items in the cartListView
            cartListView.getItems().clear();

            // Set the custom cell factory for the ListView
            cartListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(product item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Set the text to display only the name, description, and price of the product
                        setText("Name: " + item.getNom() + "\nDescription: " + item.getDescription() + "\nPrice: " + item.getPrix());
                    }
                }
            });

            // Add each product from the productList to the cartListView
            cartListView.getItems().addAll(productList);

            // Calculate total price
            calculateTotalPrice();

            System.out.println("Products added to cartListView");
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }

        // Ajouter un gestionnaire d'événements sur le bouton Remove
        remove.setOnAction(event -> {
            // Récupérer l'élément sélectionné dans le ListView
            product selectedItem = cartListView.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {

                    // Supprimer l'élément sélectionné de la liste des produits du panier
                    cartListView.getItems().remove(selectedItem);

                    // Recalculer le prix total
                    calculateTotalPrice();

            } else {
                // Afficher un message d'erreur si aucun élément n'est sélectionné
                System.out.println("No item selected to remove.");
            }
        });
    }

    private void calculateTotalPrice() {
        try {
            panierService ez = new panierService();
            // Get the list of products in the cart
            List<product> productList = new ArrayList<>(cartListView.getItems());

            // Initialize total price
            float totalPrice = 0;

            // Iterate over each product in the cart and sum up their prices
            for (product p : productList) {
                totalPrice += p.getPrix();
            }

            // Display the total price in the pricetot TextField
            pricetot.setText(String.valueOf(totalPrice));
        } catch (SQLException e) {
            System.out.println("Error calculating total price: " + e.getMessage());
        }
    }


}
