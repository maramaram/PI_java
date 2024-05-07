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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PostUpdate {

    @FXML
    private Label titleEE;
    @FXML
    private Label contenuEE;
    @FXML
    private Label dateEE;
    @FXML
    private Label imageEE;
    @FXML
    private Label viewsEE;
    @FXML
    private Label userEE;




    @FXML
    private TextField idM;
    @FXML
    private TextField titleM;
    @FXML
    private TextArea contenuM;
    @FXML
    private TextField dateM;
    @FXML
    private TextField imageM;
    @FXML
    private TextField viewsM;
    @FXML
    private TextField userM;

    private Stage primaryStage;

    public void setIdM(String idM) {
        this.idM.setText(idM);
    }

    public void setTitleM(String titleM) {
        this.titleM.setText(titleM);
    }

    public void setContenuM(String contenuM) {
        this.contenuM.setText(contenuM);
    }

    public void setDateM(String dateM) {
        this.dateM.setText(dateM);
    }

    public void setImageM(String imageM) {
        this.imageM.setText(imageM);
    }

    public void setViewsM(String viewsM) {
        this.viewsM.setText(viewsM);
    }

    public void setUserM(String userM) {
        this.userM.setText(userM);
    }

    // Méthode appelée lorsque le bouton est cliqué
    public void EX() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Post/Post-front.fxml"));
            primaryStage=(Stage)titleM.getScene().getWindow();
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
        PostService es = new PostService();
        boolean test=true;
        // Vérifier si tous les champs de texte sont remplis
        if (titleM.getText().isEmpty() ){
            test=false;
            titleEE.setText("vide");
        }else {titleEE.setText("");}


        if (contenuM.getText().isEmpty()) {
            test=false;
            contenuEE.setText("vide");
        }else {contenuEE.setText("");}


        if (dateM.getText().isEmpty()) {
            test = false;
            dateEE.setText("vide");
        } else {
            dateEE.setText("");
        }


        if (imageM.getText().isEmpty()) {
            test=false;
            imageEE.setText("vide");
        }else {imageEE.setText("");}


        if (viewsM.getText().isEmpty()) {
            test=false;
            viewsEE.setText("vide");
        }else {viewsEE.setText("");}

       if (userM.getText().isEmpty()) {
           test=false;
           userEE.setText("vide");
       }else {userEE.setText("");}





        if (test){
            List<Post> l=new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(dateM.getText());
            } catch (ParseException e) {
                e.printStackTrace(); // Handle parsing exception as needed
            }

// Now, create the Post object using the parsed Date object
            Post ex = new Post(
                    Integer.parseInt(idM.getText()),
                    titleM.getText(),
                    contenuM.getText(),
                    date,
                    imageM.getText(),
                    Integer.parseInt(viewsM.getText()),
                    Integer.parseInt(userM.getText()),
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

    public void Posts(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Post/Post-front.fxml"));
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


    public void Blogs(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Post/Post-front.fxml"));
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