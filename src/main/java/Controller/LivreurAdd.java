package Controller;

import Entities.Livreur;
import Service.LivreurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javafx.stage.FileChooser;
import javafx.scene.control.ChoiceBox;



public class LivreurAdd {

    @FXML
    private Label nomE;
    @FXML
    private Label prenomE;
    @FXML
    private Label disponibiliteE;
    @FXML
    private Label noteE;
    @FXML
    private Label imageE;



    @FXML
    private TextField nomA;
    @FXML
    private TextField prenomA;
    @FXML
    private ChoiceBox<String> disponibiliteA;
    @FXML
    private TextField noteA;
    @FXML
    private TextField imageA;
    @FXML
    private Button ib ;

    private Stage primaryStage;


    @FXML
    public void initialize() {
        disponibiliteA.getItems().addAll("disponible", "indisponible", "en livraison");

        // Action à effectuer lors du clic sur le bouton
        ib.setOnAction(e -> {
            // Création du sélecteur de fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");

            // Filtrer les types de fichiers
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.bmp", "*.jpeg"),
                    new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
            );

            // Afficher la boîte de dialogue pour sélectionner un fichier
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            // Vérifier si un fichier a été sélectionné
            if (selectedFile != null) {
                try {
                    // Définir le répertoire de destination dans les ressources
                    String destinationDirectory = "Front/images/";
                    String fileName = selectedFile.getName();

                    // Obtenir le répertoire de destination dans les ressources
                    Path destinationPath = Paths.get("src/main/resources", destinationDirectory);

                    // Créer le chemin complet du fichier de destination
                    Path destinationFilePath = destinationPath.resolve(fileName);

                    // Copier le fichier sélectionné vers le répertoire de destination dans les ressources
                    Files.copy(selectedFile.toPath(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

                    // Mettre à jour le chemin de l'image dans imgA
                    String newImagePath = destinationDirectory + fileName;
                    imageA.setText(newImagePath);

                    // Afficher un message de succès
                    System.out.println("Fichier téléchargé avec succès dans les ressources. Nouveau chemin : " + newImagePath);
                } catch (IOException ex) {
                    // Gérer les exceptions en cas d'erreur de téléchargement
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Aucun fichier sélectionné.");
            }
        });
    }

    // Méthode appelée lorsque le bouton est cliqué
    public void Lib() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Livreur/LivreurBack.fxml"));
            primaryStage=(Stage)nomA.getScene().getWindow();
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
    protected void AjouterEX() {
        LivreurService es = new LivreurService();
        boolean test=true;
        // Vérifier si tous les champs de texte sont remplis
        if (nomA.getText().isEmpty() ){
            test=false;
            nomE.setText("vide");
        }else if (!nomA.getText().matches("^[a-zA-Z]+$")) {
            test = false;
            nomE.setText("Le nom ne doit contenir que des lettres");
        }
        else {nomE.setText("");}

        if (disponibiliteA.getValue() == null) {
            test = false;
            disponibiliteE.setText("vide");
        } else {
            disponibiliteE.setText("");
        }

        if (prenomA.getText().isEmpty()) {
            test=false;
            prenomE.setText("vide");
        }else if (!prenomA.getText().matches("^[a-zA-Z]+$")) {
            test = false;
            prenomE.setText("Le prenom ne doit contenir que des lettres");
        }else {prenomE.setText("");}

        if (noteA.getText().isEmpty()) {
            test = false;
            noteE.setText("vide");
        } else {
            String noteText = noteA.getText();
            if (!noteText.matches("[0-5]")) { // Vérifier si la note est un nombre de 0 à 5
                test = false;
                noteE.setText("Veuillez saisir une note valide (de 0 à 5).");
            } else {
                noteE.setText("");
            }
        }


        String chemin = imageA.getText();
        if (chemin.isEmpty()) {
            test = false;
            imageE.setText("vide");
        }  else {
                imageE.setText(""); // Réinitialiser le champ d'erreur
            }

        if (test){
            Livreur ex = new Livreur(nomA.getText(), prenomA.getText(), disponibiliteA.getValue(),
                    imageA.getText(),Integer.parseInt(noteA.getText()));
            try{
                es.add(ex);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("Le Livreur a été a jouté avec succées !!");
                alert.showAndWait();
                Lib();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else {

            System.out.println("Veuillez remplir tous les champs.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Echec");
            alert.setContentText("Echec de l'ajout du Livreur !!");
            alert.showAndWait();
        }
    }

}