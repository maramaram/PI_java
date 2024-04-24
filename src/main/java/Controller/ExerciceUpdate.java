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


public class ExerciceUpdate {




    @FXML
    private Label nomEE;
    @FXML
    private Label desEE;
    @FXML
    private Label mcEE;
    @FXML
    private Label ndEE;
    @FXML
    private Label imgEE;
    @FXML
    private Label gifEE;




    @FXML
    private TextField idM;
    @FXML
    private TextField nomM;
    @FXML
    private TextArea desM;
    @FXML
    private TextField mcM;
    @FXML
    private TextField ndM;
    @FXML
    private TextField imgM;
    @FXML
    private TextField gifM;

    public void setIdM(String idM) {
        this.idM.setText(idM);
    }

    public void setNomM(String nomM) {
        this.nomM.setText(nomM);
    }

    public void setDesM(String desM) {
        this.desM.setText(desM);
    }

    public void setMcM(String mcM) {
        this.mcM.setText(mcM);
    }

    public void setNdM(String ndM) {
        this.ndM.setText(ndM);
    }

    public void setImgM(String imgM) {
        this.imgM.setText(imgM);
    }

    public void setGifM(String gifM) {
         this.gifM.setText(gifM);
    }

    private Stage primaryStage;

    public void EX() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/Exercice-front.fxml"));
            primaryStage=(Stage)nomM.getScene().getWindow();
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
    protected void ModifierEX() {
        ExerciceService es = new ExerciceService();
        boolean test=true;
        // Vérifier si tous les champs de texte sont remplis
        if (nomM.getText().isEmpty() ){
            test=false;
            nomEE.setText("vide");
        }else {nomEE.setText("");}


        if (desM.getText().isEmpty()) {
            test=false;
            desEE.setText("vide");
        }else {desEE.setText("");}


        if (mcM.getText().isEmpty() ||
                (!mcM.getText().equals("Pectoraux") &&
                        !mcM.getText().equals("Epaules") &&
                        !mcM.getText().equals("Biceps") &&
                        !mcM.getText().equals("Triceps") &&
                        !mcM.getText().equals("Mbdos") &&
                        !mcM.getText().equals("Dos") &&
                        !mcM.getText().equals("Quadriceps") &&
                        !mcM.getText().equals("Ischio-jambiers") &&
                        !mcM.getText().equals("Fessiers") &&
                        !mcM.getText().equals("Mollets"))) {
            test = false;
            mcEE.setText("Veuillez choisir parmi Pectoraux, Epaules, Biceps, Triceps, Mbdos, Dos, Quadriceps, Ischio-jambiers, Fessiers ou Mollets");
        } else {
            mcEE.setText("");
        }


        if (ndM.getText().isEmpty() && !ndM.getText().equals("facile") &&
                !ndM.getText().equals("moyen") &&
                !ndM.getText().equals("difficile") ) {
            test=false;
            ndEE.setText("Veuillez choisir parmi facile , moyen ou difficile");
        }else {ndEE.setText("");}


        if (imgM.getText().isEmpty()) {
            test=false;
            imgEE.setText("vide");
        }else {imgEE.setText("");}


        if (gifM.getText().isEmpty()) {
            test=false;
            gifEE.setText("vide");
        }else {gifEE.setText("");}


        if (test){
            String a;
            if(ndM.getText().equals("facile")) a="1";
            else if (ndM.getText().equals("moyen")) a="2";
            else a="3";
            Exercice ex = new Exercice(Integer.parseInt(idM.getText()),nomM.getText(), desM.getText(), mcM.getText(),
                   a, imgM.getText(), gifM.getText());
            try{
                es.modifier(ex);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("L'exercice a été a Modifié avec succées !!");
                alert.showAndWait();
                EX();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else {

            System.out.println("Veuillez remplir tous les champs.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Echec");
            alert.setContentText("Echec de la modification de l'exercice !!");
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