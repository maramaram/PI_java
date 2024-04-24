package Controller;

import Entities.panier;
import Entities.product;
import Service.panierService;
import Service.productService;
import com.example.projectpi.HelloApplication;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class panierController extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/paniers/basebackp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    @FXML
    private TableView<panier> table_panier;

    @FXML
    private TableColumn<panier, Integer> col_id;

    @FXML
    private TableColumn<panier, Integer> col_prix_tot;

    @FXML
    private TableColumn<panier, Integer> col_quantite;

    @FXML
    private TextField txt_prix_tot;

    @FXML
    private TextField txt_quantite;
    @FXML
    private ComboBox<String> comboBoxProduits;

    // Instance de panierService
    private panierService pService;

    public panierController() throws SQLException {
        pService = new panierService();


    }

    @FXML
    void ajouterpanier(ActionEvent event) {
        if (validateFields()) {
            try {
                // Créer un panier vide
                int panierId = pService.creerPanier();

                // Récupérer le produit sélectionné
                String produitNom = comboBoxProduits.getSelectionModel().getSelectedItem();
                productService productService = new productService();
                product selectedProduct = productService.getProduitByNom(produitNom);

                if (selectedProduct == null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Produit non trouvé !");
                    return;
                }

                // Ajouter le produit au panier
                pService.ajouterProduitAuPanier(panierId, selectedProduct.getId());

                // Récupérer les valeurs des champs de texte
                int prixTot = Integer.parseInt(txt_prix_tot.getText());
                int quantite = Integer.parseInt(txt_quantite.getText());

                // Mettre à jour le panier avec les valeurs de prixTot et quantite
                panier updatedPanier = new panier(panierId, prixTot, quantite);
                pService.updatepanier(panierId, updatedPanier);

                // Actualiser l'affichage du panier après l'ajout
                afficherPanier();

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Produits ajoutés au panier avec succès !");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout des produits au panier !");
            }
        }
    }

    private boolean validateFields() {
        // Validation pour le champ Prix Total
        if (txt_prix_tot.getText().isEmpty() || Integer.parseInt(txt_prix_tot.getText()) <= 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le Prix Total doit être un nombre positif !");
            return false;
        }

        // Validation pour le champ Quantité
        if (txt_quantite.getText().isEmpty() || Integer.parseInt(txt_quantite.getText()) <= 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La Quantité doit être un entier positif !");
            return false;
        }

        return true;
    }


    @FXML
    void deletepanier(ActionEvent event) {
        // Récupérer l'ID du panier sélectionné dans le TableView
        int id = table_panier.getSelectionModel().getSelectedItem().getId();

        try {
            // Appeler la méthode de suppression de panierService
            pService.deletepanier(id);
            afficherPanier(); // Actualiser l'affichage du panier après la suppression
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Panier supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression du panier !");
        }
    }
    @FXML
    public void initialize() {
        // Initialisez le TableView avec les données des produits
        initTableView();
        initComboBox();
        // Ajoutez un gestionnaire d'événements de sélection sur le TableView
        table_panier.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Récupérez le produit sélectionné
                panier selectedpanier = table_panier.getSelectionModel().getSelectedItem();

                // Affichez les données du produit sélectionné dans les champs de texte correspondants
                txt_prix_tot.setText(String.valueOf(selectedpanier.getPrix_tot()));
                txt_quantite.setText(String.valueOf(selectedpanier.getQuantite()));

            }
        });
    }
    private void initComboBox() {
        try {
            productService pService = new productService();
            List<product> productList = pService.afficherList();

            ObservableList<String> productNames = FXCollections.observableArrayList();
            for (product p : productList) {
                productNames.add(p.getNom());
            }

            comboBoxProduits.setItems(productNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initTableView() {
        try {
            panierService pService = new panierService(); // Créez une instance de productService
            List<panier> panierList = pService.afficherp(); // Récupérez la liste des produits depuis la base de données
            ObservableList<panier> observablepanierList = FXCollections.observableArrayList(panierList); // Créez une liste observable à partir de la liste de produits

            // Associez chaque colonne à une propriété du modèle de données
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_prix_tot.setCellValueFactory(new PropertyValueFactory<>("prix_tot"));
            col_quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));


            // Remplissez le TableView avec les données des produits
            table_panier.setItems(observablepanierList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception
        }
    }
    @FXML
    void updatepanier(ActionEvent event) {
        // Récupérer l'ID et les nouvelles valeurs du panier à mettre à jour
        int id = table_panier.getSelectionModel().getSelectedItem().getId();
        int nouveauPrixTot = Integer.parseInt(txt_prix_tot.getText());
        int nouvelleQuantite = Integer.parseInt(txt_quantite.getText());

        // Créer un objet panier avec les nouvelles valeurs
        panier updatedPanier = new panier(id, nouveauPrixTot, nouvelleQuantite);

        try {
            // Appeler la méthode de mise à jour de panierService
            pService.updatepanier(id, updatedPanier);
            afficherPanier(); // Actualiser l'affichage du panier après la mise à jour
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Panier mis à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour du panier !");
        }
    }

    // Méthode pour afficher le panier dans le TableView
    private void afficherPanier() throws SQLException {
        List<panier> paniers = pService.afficherp();
        ObservableList<panier> observablePaniers = FXCollections.observableArrayList(paniers);
        table_panier.setItems(observablePaniers);
    }

    // Méthode pour afficher une boîte de dialogue d'alerte
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
  /*  @FXML
    void onProduitSelected(ActionEvent event) {
        try {
            // Créer un panier vide
            int panierId = pService.creerPanier();

            // Récupérer le produit sélectionné
            String produitNom = comboBoxProduits.getSelectionModel().getSelectedItem();
            productService productService = new productService();
            product selectedProduct = productService.getProduitByNom(produitNom);

            if (selectedProduct == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Produit non trouvé !");
                return;
            }

            // Ajouter le produit au panier
            pService.ajouterProduitAuPanier(panierId, selectedProduct.getId());

            // Mettre à jour le panier avec les valeurs de prixTot et quantite
            int prixTot = Integer.parseInt(txt_prix_tot.getText());
            int quantite = Integer.parseInt(txt_quantite.getText());
            panier updatedPanier = new panier(panierId, prixTot, quantite);
            pService.updatepanier(panierId, updatedPanier);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Produits ajoutés au panier avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout des produits au panier !");
        }
    }*/





    private void afficherProduitsDuPanier(int panierId) {
        try {
            List<product> produits = pService.getProduitsDuPanier(panierId); // Vérifiez cette méthode
            ObservableList<product> observableProduits = FXCollections.observableArrayList(produits);

            // Mettez à jour votre TableView avec la liste des produits
            // Assurez-vous que les colonnes du TableView correspondent aux propriétés du modèle 'product'
            // Par exemple, pour une colonne 'nom' :
            // col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));


        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'affichage des produits du panier !");
        }
    }



}
