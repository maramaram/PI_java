package Controller;

import Entities.Livreur;
import Service.LivreurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class LivreurFront {


    private Stage primaryStage;



    @FXML
    private TableView table;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn nom;
    @FXML
    private TableColumn prenom;
    @FXML
    private TableColumn disponibilite;
    @FXML
    private TableColumn note;
    @FXML
    private TableColumn image;

    @FXML
    private TextField search;



    @FXML
    public void initialize() {
        AfficherEX(); // Appeler la méthode pour afficher les données

        // Ajouter un écouteur sur la table pour détecter les double-clics
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifier si c'est un double-clic
                // Vérifier le type de l'objet sélectionné
                Object selectedItem = table.getSelectionModel().getSelectedItem();
                if (selectedItem instanceof Livreur) {
                    Livreur Livreur = (Livreur) selectedItem;
                    try {
                        // Charger le fichier FXML LivreurUpdate.fxml
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Livreur/LivreurUpdate.fxml"));
                        Parent root = fxmlLoader.load();

                        // Obtenir le contrôleur associé au fichier FXML chargé
                        LivreurUpdate LivreurUpdate = fxmlLoader.getController();

                        // Remplir les champs du contrôleur avec les données de l'Livreur sélectionné
                        LivreurUpdate.setIdA(String.valueOf(Livreur.getId()));
                        LivreurUpdate.setNomA(Livreur.getNom());
                        LivreurUpdate.setPrenomA(Livreur.getPrenom());
                        LivreurUpdate.setDisponibiliteA(Livreur.getDisponibilite());
                        LivreurUpdate.setNoteA(String.valueOf(Livreur.getNote()));
                        LivreurUpdate.setImageA(Livreur.getImage());

                        // Remplacer la racine de la scène par le nouveau contenu chargé à partir du fichier FXML
                        table.getScene().setRoot(root);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @FXML
    protected void supprimerEX() {

        TableView.TableViewSelectionModel<Livreur> selectionModel = table.getSelectionModel();

// Vérifier s'il y a une ligne sélectionnée
        if (!selectionModel.isEmpty()) {
            LivreurService es = new LivreurService();
            // Récupérer l'objet Livreur correspodisponibiliteant à la ligne sélectionnée
            Livreur selectedLivreur = selectionModel.getSelectedItem();
            try {
                es.delete(selectedLivreur);
                AfficherEX();
            }catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }

    @FXML
    protected void AfficherEX() {
        LivreurService es = new LivreurService();
        try {
            List<Livreur> l = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Livreur Livreur : l) {
                table.getItems().add(Livreur);
            }

            // Associer les propriétés prenom objets Livreur aux colonnes de la TableView
            if (this.id != null) {
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
            }

            if (this.nom != null) {
                nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            }

            if (this.prenom != null) {
                prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            }

            if (this.disponibilite != null) {
                disponibilite.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
            }

            if (this.note != null) {
                note.setCellValueFactory(new PropertyValueFactory<>("note"));
            }
            if (this.image != null) {
                image.setCellValueFactory(new PropertyValueFactory<>("img"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void AJ(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Livreur/LivreurAdd.fxml"));
            primaryStage=(Stage)((Node)event.getSource()).getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }




    @FXML
    protected void AfficherEXSearch() {
        LivreurService es = new LivreurService();
        try {
            List<Livreur> l = es.afficherListSearch(search.getText());

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Livreur Livreur : l) {
                table.getItems().add(Livreur);
            }

            // Associer les propriétés prenom objets Livreur aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            disponibilite.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
            note.setCellValueFactory(new PropertyValueFactory<>("note"));
            image.setCellValueFactory(new PropertyValueFactory<>("img"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

