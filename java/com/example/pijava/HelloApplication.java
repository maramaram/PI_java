package com.example.pijava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //         /com/example/pijava/AdminPannel.fxml        /Exercice/ExerciceV.fxml         /Defi/DefiV.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Exercice/ExerciceV.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BreathOut");
        stage.setScene(scene);
        stage.show();
    }
// TODO   TRI FONT    /   LIKES OR RATING OR FAV    /   API CHATGPT   /   API ??   /  UPLOAD IMAGE  / pagination
    public static void main(String[] args) {
        launch();
    }
}