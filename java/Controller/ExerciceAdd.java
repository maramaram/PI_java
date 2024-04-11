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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class ExerciceAdd {

    @FXML
    private Label nomE;
    @FXML
    private Label desE;
    @FXML
    private Label mcE;
    @FXML
    private Label ndE;
    @FXML
    private Label imgE;
    @FXML
    private Label gifE;



    @FXML
    private TextField nomA;
    @FXML
    private TextArea desA;
    @FXML
    private TextField mcA;
    @FXML
    private TextField ndA;
    @FXML
    private TextField imgA;
    @FXML
    private TextField gifA;


    private Stage primaryStage;




    // Méthode appelée lorsque le bouton est cliqué
    public void EX() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/Exercice-front.fxml"));
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
        ExerciceService es = new ExerciceService();
boolean test=true;
        // Vérifier si tous les champs de texte sont remplis
        if (nomA.getText().isEmpty() ){
            test=false;
            nomE.setText("vide");
        }else {nomE.setText("");}


        if (desA.getText().isEmpty()) {
            test=false;
            desE.setText("vide");
        }else {desE.setText("");}


        if (mcA.getText().isEmpty() ||
                (!mcA.getText().equals("Pectoraux") &&
                        !mcA.getText().equals("Epaules") &&
                        !mcA.getText().equals("Biceps") &&
                        !mcA.getText().equals("Triceps") &&
                        !mcA.getText().equals("Abdos") &&
                        !mcA.getText().equals("Dos") &&
                        !mcA.getText().equals("Quadriceps") &&
                        !mcA.getText().equals("Ischio-jambiers") &&
                        !mcA.getText().equals("Fessiers") &&
                        !mcA.getText().equals("Mollets"))) {
            test = false;
            mcE.setText("Veuillez choisir parmi Pectoraux, Epaules, Biceps, Triceps, Abdos, Dos, Quadriceps, Ischio-jambiers, Fessiers ou Mollets");
        } else {
            mcE.setText("");
        }


        if (ndA.getText().isEmpty() && !ndA.getText().equals("facile") &&
                !ndA.getText().equals("moyen") &&
                !ndA.getText().equals("difficile") ) {
            test=false;
            ndE.setText("Veuillez choisir parmi facile , moyen ou difficile");
        }else {ndE.setText("");}


        if (imgA.getText().isEmpty()) {
            test=false;
            imgE.setText("vide");
        }else {imgE.setText("");}


        if (gifA.getText().isEmpty()) {
            test=false;
            gifE.setText("vide");
        }else {gifE.setText("");}


           if (test){
            String a;
            if(ndA.getText().equals("facile")) a="1";
            else if (ndA.getText().equals("moyen")) a="2";
             else a="3";

            Exercice ex = new Exercice(nomA.getText(), desA.getText(), mcA.getText(),
                    a, imgA.getText(), gifA.getText());
            try{
            es.add(ex);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("L'exercice a été a jouté avec succées !!");
                alert.showAndWait();
                EX();
           } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        } else {

            System.out.println("Veuillez remplir tous les champs.");
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Echec");
               alert.setContentText("Echec de l'ajout de l'exercice !!");
               alert.showAndWait();
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