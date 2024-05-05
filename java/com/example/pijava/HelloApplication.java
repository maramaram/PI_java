package com.example.pijava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //         /com/example/pijava/AdminPannel.fxml        /Exercice/ExerciceV.fxml         /Defi/DefiV.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Exercice/ExerciceV.fxml"));
        Image icon = new Image(this.getClass().getResourceAsStream("/Front/images/icon.png"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BreathOut");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
// TODO   TRI FONT    /    API CHATGPT    /   API PDF   /     API EXCEL    /    UPLOAD IMAGE   /    pagination   /    notification    /    search  /    sound to text
    public static void main(String[] args) {
        launch();
    }
}