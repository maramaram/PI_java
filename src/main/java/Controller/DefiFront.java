package Controller;

import Entities.Defi;
import Service.DefiService;
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


public class DefiFront {


    private Stage primaryStage;



    @FXML
    private TableView table;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn nom;
    @FXML
    private TableColumn des;
    @FXML
    private TableColumn nd;
    @FXML
    private TableColumn nbj;
    @FXML
    private TableColumn ex;

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
                if (selectedItem instanceof Defi) {
                    Defi defi = (Defi) selectedItem;
                    try {
                        // Charger le fichier FXML DefiUpdate.fxml
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Defi/DefiUpdate.fxml"));
                        Parent root = fxmlLoader.load();

                        // Obtenir le contrôleur associé au fichier FXML chargé
                        DefiUpdate defiUpdate = fxmlLoader.getController();

                        // Remplir les champs du contrôleur avec les données de l'defi sélectionné
                        defiUpdate.setIdM(String.valueOf(defi.getId()));
                        defiUpdate.setNomM(defi.getNom());
                        defiUpdate.setDesM(defi.getDes());
                        defiUpdate.setNdM(defi.getNd());
                        defiUpdate.setNbjM(String.valueOf(defi.getNbj()));
                        defiUpdate.setEx(defi.getLex());

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

        TableView.TableViewSelectionModel<Defi> selectionModel = table.getSelectionModel();

// Vérifier s'il y a une ligne sélectionnée
        if (!selectionModel.isEmpty()) {
            DefiService es = new DefiService();
            // Récupérer l'objet Defi correspondant à la ligne sélectionnée
            Defi selectedDefi = selectionModel.getSelectedItem();
            try {
            es.delete(selectedDefi);
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
        DefiService es = new DefiService();
        try {
            List<Defi> l = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Defi defi : l) {
                table.getItems().add(defi);
            }

            // Associer les propriétés des objets Defi aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            des.setCellValueFactory(new PropertyValueFactory<>("des"));
            nd.setCellValueFactory(new PropertyValueFactory<>("nd"));
            nbj.setCellValueFactory(new PropertyValueFactory<>("nbj"));
            ex.setCellValueFactory(new PropertyValueFactory<>("nomx"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void AJ(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/DefiAdd.fxml"));
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
        DefiService es = new DefiService();
        try {
            List<Defi> l = es.afficherListSearch(search.getText());

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Defi defi : l) {
                table.getItems().add(defi);
            }

            // Associer les propriétés des objets Defi aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            des.setCellValueFactory(new PropertyValueFactory<>("des"));
            nd.setCellValueFactory(new PropertyValueFactory<>("nd"));
            nbj.setCellValueFactory(new PropertyValueFactory<>("nbj"));
            ex.setCellValueFactory(new PropertyValueFactory<>("nomx"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }









    public void Exercices(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/Exercice-front.fxml"));
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

    public void Defis(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/Defi-front.fxml"));
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


}

