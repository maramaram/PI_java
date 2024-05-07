package tn.esprit.javafxproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {///Fxml/AdminPannel.fxml
        Parent parent = FXMLLoader.load(getClass().getResource("/Fxml/AdminPannel.fxml"));
        Scene scene = new Scene(parent);
        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
        stage.getIcons().add(icon);
        stage.setTitle("Welcome");
    stage.setScene(scene);
    stage.show();
    }
public static void main(String[] args) {
        launch();
}
}
