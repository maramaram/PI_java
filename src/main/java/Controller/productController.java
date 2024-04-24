package Controller;

import Entities.product;
import Service.productService;
import com.example.projectpi.HelloApplication;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class productController extends Application {



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/products/baseback.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @FXML
    private TableColumn<?, ?> col_categorie;

    @FXML
    private TableColumn<?, ?> col_description;

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_image;

    @FXML
    private TableColumn<?, ?> col_nom;

    @FXML
    private TableColumn<?, ?> col_prix;

    @FXML
    private TableColumn<?, ?> col_quantite;

    @FXML
    private TableView<product> table_produits;

    @FXML
    private TextField txt_categorie;

    @FXML
    private TextField txt_description;

    @FXML
    private TextField txt_nom;

    @FXML
    private TextField txt_prix;

    @FXML
    private TextField txt_quantite;
    @FXML
    private TextField txt_image;
    @FXML
    void ajouterproduit(ActionEvent event) throws SQLException {
        if (validateFields()) {
            productService pService = new productService(); // Créez une instance de productService
            product pr = new product(txt_nom.getText(), txt_description.getText(), Integer.parseInt(txt_quantite.getText()), txt_categorie.getText(), Float.parseFloat(txt_prix.getText()), txt_image.getText());
            try {
                pService.ajouterproduit(pr); // Appelez la méthode sur l'instance créée
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le produit a été ajouté avec succès !");
                alert.showAndWait();
                initTableView();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Échec");
                alert.setContentText("Échec de l'ajout du produit !");
                alert.showAndWait();
            }
        }
    }
    private boolean validateFields() {
        // Validation pour le champ Nom
        if (txt_nom.getText().isEmpty()) {
            showAlert("Erreur", "Le champ Nom ne doit pas être vide !");
            return false;
        }

        // Validation pour le champ Description
        if (txt_description.getText().isEmpty()) {
            showAlert("Erreur", "Le champ Description ne doit pas être vide !");
            return false;
        }

        // Validation pour le champ Quantité
        if (txt_quantite.getText().isEmpty() || Integer.parseInt(txt_quantite.getText()) <= 0) {
            showAlert("Erreur", "La Quantité doit être un entier positif !");
            return false;
        }

        // Validation pour le champ Catégorie


        // Validation pour le champ Prix
        if (txt_prix.getText().isEmpty() || Float.parseFloat(txt_prix.getText()) <= 0) {
            showAlert("Erreur", "Le Prix doit être un nombre positif !");
            return false;
        }
        if (txt_image.getText().isEmpty() || !validateImageExtension(txt_image.getText())) {
            showAlert("Erreur", "L'extension de l'image n'est pas valide ! Les extensions autorisées sont : jpg, jpeg, png, gif");
            return false;
        }
        // Validation pour le champ Image (ajoutez votre propre validation ici)

        return true;
    }
    private boolean validateImageExtension(String imageName) {
        String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"};
        String extension = imageName.substring(imageName.lastIndexOf(".") + 1).toLowerCase();

        for (String ext : allowedExtensions) {
            if (ext.equals(extension)) {
                return true;
            }
        }

        return false;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void initialize() {
        // Initialisez le TableView avec les données des produits
        initTableView();

        // Ajoutez un gestionnaire d'événements de sélection sur le TableView
        table_produits.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Récupérez le produit sélectionné
                product selectedProduct = table_produits.getSelectionModel().getSelectedItem();

                // Affichez les données du produit sélectionné dans les champs de texte correspondants
                txt_nom.setText(selectedProduct.getNom());
                txt_description.setText(selectedProduct.getDescription());
                txt_quantite.setText(String.valueOf(selectedProduct.getQuantite_stock()));
                txt_categorie.setText(selectedProduct.getCategories());
                txt_prix.setText(String.valueOf(selectedProduct.getPrix()));
                txt_image.setText(selectedProduct.getImage());
            }
        });
        // Contrôle de saisie pour le champ Quantité
        txt_quantite.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Vérifie que la saisie est un entier
                txt_quantite.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Contrôle de saisie pour le champ Prix
        txt_prix.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) { // Vérifie que la saisie est un nombre
                txt_prix.setText(newValue.replaceAll("[^\\d\\.]", ""));
            }
        });

    }

    private void initTableView() {
        try {
            productService pService = new productService(); // Créez une instance de productService
            List<product> productList = pService.afficherList(); // Récupérez la liste des produits depuis la base de données
            ObservableList<product> observableProductList = FXCollections.observableArrayList(productList); // Créez une liste observable à partir de la liste de produits

            // Associez chaque colonne à une propriété du modèle de données
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_quantite.setCellValueFactory(new PropertyValueFactory<>("quantite_stock"));
            col_categorie.setCellValueFactory(new PropertyValueFactory<>("categories"));
            col_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            col_image.setCellValueFactory(new PropertyValueFactory<>("img"));

            // Remplissez le TableView avec les données des produits
            table_produits.setItems(observableProductList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception
        }
    }



    @FXML
    void deleteproduit(ActionEvent event) throws SQLException {
        product selectedProduct = table_produits.getSelectionModel().getSelectedItem(); // Obtenez le produit sélectionné dans le TableView
        if (selectedProduct != null) {
            try {
                productService pService = new productService(); // Créez une instance de productService
                pService.deleteproduit(selectedProduct.getId()); // Appelez la méthode deleteproduit avec l'ID du produit sélectionné
                // Supprimez le produit de la liste observable
                table_produits.getItems().remove(selectedProduct);
                // Affichez un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le produit a été supprimé avec succès !");
                alert.showAndWait();
            } catch (SQLException e) {
                // Gérez les erreurs de suppression
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Une erreur s'est produite lors de la suppression du produit !");
                alert.showAndWait();
            }
        } else {
            // Affichez un message d'erreur si aucun produit n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez sélectionner un produit à supprimer !");
            alert.showAndWait();
        }
    }

    @FXML
    void updateProduit(ActionEvent event) throws SQLException {
        productService pService = new productService();
        product selectedProduct = table_produits.getSelectionModel().getSelectedItem();
        if (selectedProduct != null && validateFields()) {
            try {
                selectedProduct.setNom(txt_nom.getText());
                selectedProduct.setDescription(txt_description.getText());
                selectedProduct.setQuantite_stock(Integer.parseInt(txt_quantite.getText()));
                selectedProduct.setCategories(txt_categorie.getText());
                selectedProduct.setPrix(Float.parseFloat(txt_prix.getText()));

                // Mettez à jour l'image seulement si un nouveau chemin d'image est spécifié
                if (!txt_image.getText().isEmpty()) {
                    selectedProduct.setImage(txt_image.getText());
                }

                pService.updateProduit(selectedProduct.getId(), selectedProduct);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le produit a été mis à jour avec succès !");
                alert.showAndWait();
                initTableView();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Une erreur s'est produite lors de la mise à jour du produit !");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez vérifier les champs !");
            alert.showAndWait();
        }
    }



   /* @FXML
    public void initialize() {
        try {
            // Créez une instance de productService
            productService pService = new productService();

            // Récupérez la liste des produits depuis la base de données
            List<product> productList = pService.afficherList();

            // Créez une liste observable à partir de la liste de produits
            ObservableList<product> observableProductList = FXCollections.observableArrayList(productList);

            // Associez chaque colonne à une propriété du modèle de données
            col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_quantite.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
            col_categorie.setCellValueFactory(new PropertyValueFactory<>("categories"));
            col_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            col_image.setCellValueFactory(new PropertyValueFactory<>("image"));

            // Remplissez le TableView avec les données des produits
            table_produits.setItems(observableProductList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception
        }
    }*/

    @FXML
    private void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", ".jpg", ".jpeg", ".png", ".gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            txt_image.setText(selectedFile.getAbsolutePath());
        }
    }


}