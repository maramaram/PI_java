package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationController {

    public void Exercices(ActionEvent event) {
        loadFXML("/Exercice/Exercice-front.fxml", event);
    }
    public void Defis(ActionEvent event) {
        loadFXML("/Defi/Defi-front.fxml", event);
    }
    public void Commande(ActionEvent event) {
        loadFXML("/Commande/CommandeBack.fxml", event);
    }
    public void Livreur(ActionEvent event) {
        loadFXML("/Livreur/LivreurBack.fxml", event);
    }
    public void Blogs(ActionEvent event) {
        loadFXML("/Post/Post-front.fxml", event);
    }
    public void Comment(ActionEvent event) {
        loadFXML("/Comment/Comment-front.fxml", event);
    }
    private void loadFXML(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }
}
