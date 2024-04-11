package Controller;

import Entities.Exercice;
import Service.ExerciceService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ExerciceDetails {


    private Stage primaryStage;


    @FXML
    private Label nomM;
    @FXML
    private Label desM;
    @FXML
    private Label mcM;
    @FXML
    private ImageView ndM;
    @FXML
    private ImageView image;



    public void setNomM(String nomM) {
        this.nomM.setText(nomM);
        this.nomM.setStyle("-fx-alignment: center; -fx-font-size: 30; -fx-font-weight: bold;");
    }

    public void setDesM(String desM) {
        this.desM.setText(desM);
        this.desM.setWrapText(true);
    }

    public void setMcM(String mcM) {
        this.mcM.setText(mcM);
        this.mcM.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    }

    public void setNdM(String ndM) {
        if (ndM != null) {
            if (ndM.equals("1")) {
                this.ndM.setImage(new Image(this.getClass().getResourceAsStream("/Front/images/exo/dif1.png")));
            } else if (ndM.equals("2")) {
                this.ndM.setImage(new Image(this.getClass().getResourceAsStream("/Front/images/exo/dif2.png")));
            } else {
                this.ndM.setImage(new Image(this.getClass().getResourceAsStream("/Front/images/exo/dif3.png")));
            }
        } else {
            // Gérer le cas où ndM est null
            // Par exemple, vous pouvez définir une image par défaut
            this.ndM.setImage(new Image(this.getClass().getResourceAsStream("/Front/images/exo/default.png")));
        }
        this.ndM.setFitWidth(75);
        this.ndM.setFitHeight(50);
    }

    public void setIiimage(String imgM) {
        this.image.setImage(new Image(this.getClass().getResourceAsStream("/"+imgM)));
        this.image.setStyle("-fx-alignment: center;");

    }






    public void Defis( ) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/DefiV.fxml"));
            primaryStage=(Stage)desM.getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }

    public void Exercices() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/ExerciceV.fxml"));
            primaryStage=(Stage)desM.getScene().getWindow();
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

