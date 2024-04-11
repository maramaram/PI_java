package Controller;
import Entities.Exercice;
import Service.ExerciceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.sql.SQLException;
import javafx.scene.control.TableCell;



public class ExerciceFront {


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
    private TableColumn mc;
    @FXML
    private TableColumn nd;
    @FXML
    private TableColumn img;
    @FXML
    private TableColumn gif;

    @FXML
    private TextField search;


    @FXML
    public void initialize() {
        AfficherEX(); // Appeler la méthode pour afficher les données

        // Ajouter un écouteur sur la table pour détecter les double-clics
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifier si c'est un double-clic
                // Convertir l'objet sélectionné en Exercice en utilisant le casting
                Exercice exercice = (Exercice) table.getSelectionModel().getSelectedItem();
                if (exercice != null) {
                    try {
                        // Charger le fichier FXML ExerciceUpdate.fxml
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Exercice/ExerciceUpdate.fxml"));
                        Parent root = fxmlLoader.load();

                        // Obtenir le contrôleur associé au fichier FXML chargé
                        ExerciceUpdate exerciceUpdate = fxmlLoader.getController();

                        // Remplir les champs du contrôleur avec les données de l'exercice sélectionné
                        exerciceUpdate.setIdM(String.valueOf(exercice.getId()));
                        exerciceUpdate.setNomM(exercice.getNom());
                        exerciceUpdate.setDesM(exercice.getDes());
                        exerciceUpdate.setMcM(exercice.getMc());
                        exerciceUpdate.setNdM(exercice.getNd());
                        exerciceUpdate.setImgM(exercice.getImg());
                        exerciceUpdate.setGifM(exercice.getGif());

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

        TableView.TableViewSelectionModel<Exercice> selectionModel = table.getSelectionModel();

// Vérifier s'il y a une ligne sélectionnée
        if (!selectionModel.isEmpty()) {
            ExerciceService es = new ExerciceService();
            // Récupérer l'objet Exercice correspondant à la ligne sélectionnée
            Exercice selectedExercice = selectionModel.getSelectedItem();
            try {
            es.delete(selectedExercice);
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
        ExerciceService es = new ExerciceService();
        try {
            List<Exercice> l = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Exercice exercice : l) {
                table.getItems().add(exercice);
            }

            // Associer les propriétés des objets Exercice aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            des.setCellValueFactory(new PropertyValueFactory<>("des"));
            mc.setCellValueFactory(new PropertyValueFactory<>("mc"));
            nd.setCellValueFactory(new PropertyValueFactory<>("nd"));
            img.setCellValueFactory(new PropertyValueFactory<>("image"));
            gif.setCellValueFactory(new PropertyValueFactory<>("gif"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void AJ(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/ExerciceAdd.fxml"));
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
        ExerciceService es = new ExerciceService();
        try {
            List<Exercice> l = es.afficherListSearch(search.getText());

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Exercice exercice : l) {
                table.getItems().add(exercice);
            }

            // Associer les propriétés des objets Exercice aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            des.setCellValueFactory(new PropertyValueFactory<>("des"));
            mc.setCellValueFactory(new PropertyValueFactory<>("mc"));
            nd.setCellValueFactory(new PropertyValueFactory<>("nd"));
            img.setCellValueFactory(new PropertyValueFactory<>("image"));
            gif.setCellValueFactory(new PropertyValueFactory<>("gif"));

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

