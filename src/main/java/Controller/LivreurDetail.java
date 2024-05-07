package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import Entities.Livreur;
import Service.LivreurService;
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


public class LivreurDetail {


    private Stage primaryStage;


    @FXML
    private Label nomM;
    @FXML
    private Label prenomM;
    @FXML
    private Label disponibiliteM;
    @FXML
    private Label noteM;
    @FXML
    private ImageView image;



    public void setNomM(String nomM) {
        this.nomM.setText(nomM);
        this.nomM.setStyle("-fx-alignment: center; -fx-font-size: 30; -fx-font-weight: bold;");
    }

    public void setprenomM(String prenomM) {
        this.prenomM.setText(prenomM);
        this.prenomM.setStyle("-fx-alignment: center; -fx-font-size: 30; -fx-font-weight: bold;");
    }

    public void setdisponibiliteM(String disponibiliteM) {
        this.disponibiliteM.setText(disponibiliteM);
        this.disponibiliteM.setStyle("-fx-alignment: center; -fx-font-size: 30; -fx-font-weight: bold;");
    }

    public void setnoteM(String noteM) {
        this.noteM.setText(noteM);
        this.noteM.setStyle("-fx-alignment: center; -fx-font-size: 30; -fx-font-weight: bold;");
    }

    public void setImage(String imgM) {
        this.image.setImage(new Image(this.getClass().getResourceAsStream("/"+imgM)));
        this.image.setStyle("-fx-alignment: center;");

    }

}

