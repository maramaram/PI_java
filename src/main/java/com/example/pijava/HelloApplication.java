package com.example.pijava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ///com/example/pijava/AdminPannel.fxml   /Exercice/ExerciceV.fxml /Defi/DefiV.fxml /Commande/CommandeV.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Commande/CommandeBack.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BreathOut");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}