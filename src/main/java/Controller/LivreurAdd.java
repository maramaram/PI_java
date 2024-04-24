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
    private TextField disponibiliteA;
    @FXML
    private TextField noteA;
    @FXML
    private TextField imageA;


    private Stage primaryStage;




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


        if (prenomA.getText().isEmpty()) {
            test=false;
            prenomE.setText("vide");
        }else if (!prenomA.getText().matches("^[a-zA-Z]+$")) {
            test = false;
            prenomE.setText("Le prenom ne doit contenir que des lettres");
        }else {prenomE.setText("");}


        if (disponibiliteA.getText().isEmpty()) {
            test = false;
            disponibiliteE.setText("vide");
        }else if (!disponibiliteA.getText().equals("en livraison") &&
                !disponibiliteA.getText().equals("indisponible") &&
                !disponibiliteA.getText().equals("disponible")) {
            test = false;
            disponibiliteE.setText("Veuillez choisir parmi disponible , indisponible et en livraison");
        } else {
            disponibiliteE.setText("");
        }


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
        } else if (!chemin.startsWith("Front/images/")) {
            test = false;
            imageE.setText("Le chemin doit commencer par 'Front/images/'");
        } else {
            // Vérifier si le fichier existe
            File fichier = new File(chemin);
            if (!fichier.exists()) {
                test = false;
                imageE.setText("Le fichier spécifié n'existe pas.");
            } else {
                imageE.setText(""); // Réinitialiser le champ d'erreur
            }
        }






        if (test){
           

            Livreur ex = new Livreur(nomA.getText(), prenomA.getText(), disponibiliteA.getText(),
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






    public void Livreur(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Livreur/LivreurBack.fxml"));
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