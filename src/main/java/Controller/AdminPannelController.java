package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPannelController {

    private Stage primaryStage;

    private Scene scene;

    private Parent root;


    // Méthode appelée lorsque le bouton est cliqué
    public void Session(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Sessions-front.fxml"));
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }

    public void Reservation(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Reservation-front.fxml"));
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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