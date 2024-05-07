package Controller;

import Entities.Post;
import Service.PostService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PostModV {

    @FXML
    private Label titleE;
    @FXML
    private Label contenuE;

    @FXML
    private Label imageE;



    @FXML
    private TextField idA;
    @FXML
    private TextField titleA;
    @FXML
    private TextArea contenuA;
    @FXML
    private TextField imageA;
    @FXML
    private TextField dateA;
    @FXML
    private TextField viewsA;
    @FXML
    private TextField userA;


    private Stage primaryStage;

    public void setIdA(String idM) {this.idA.setText(idM);}

    public void setTitleA(String titleM) {
        this.titleA.setText(titleM);
    }

    public void setContenuA(String contenuM) {
        this.contenuA.setText(contenuM);
    }

    public void setDateA(String dateM) {
        this.dateA.setText(dateM);
    }

    public void setImageA(String imageM) {
        this.imageA.setText(imageM);
    }

    public void setViewsA(String viewsM) {
        this.viewsA.setText(viewsM);
    }

    public void setUserA(String userM) {
        this.userA.setText(userM);
    }




    // Méthode appelée lorsque le bouton est cliqué
    public void EX() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Post/PostV.fxml"));
            primaryStage=(Stage)titleA.getScene().getWindow();
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
        PostService es = new PostService();
        boolean test=true;
        // Vérifier si tous les champs de texte sont remplis
        if (titleA.getText().isEmpty() ){
            test=false;
            titleE.setText("vide");
        }else {titleE.setText("");}

        if (contenuA.getText().isEmpty()) {
            test=false;
            contenuE.setText("vide");
        }else {contenuE.setText("");}

        if (imageA.getText().isEmpty()) {
            test=false;
            imageE.setText("vide");
        }else {imageE.setText("");}

        if (test){
            List<Post> l=new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(dateA.getText());
            } catch (ParseException e) {
                e.printStackTrace(); // Handle parsing exception as needed
            }

// Now, create the Post object using the parsed Date object
            Post ex = new Post(
                    Integer.parseInt(idA.getText()),
                    titleA.getText(),
                    contenuA.getText(),
                    date,
                    imageA.getText(),
                    Integer.parseInt(viewsA.getText()),
                    Integer.parseInt(userA.getText()),
                    new ArrayList<>()
            );
            try{
                es.modifier(ex);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("Le post a été modifier avec succées !!");
                alert.showAndWait();
                EX();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else {

            System.out.println("Veuillez remplir tous les champs.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Echec");
            alert.setContentText("Echec de modification de post !!");
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

    @FXML
    public void Blogs(MouseEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène
            Parent root = FXMLLoader.load(getClass().getResource("/Post/PostV.fxml"));

            // Obtenir la scène à partir de l'événement source
            primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la nouvelle scène à la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }




}