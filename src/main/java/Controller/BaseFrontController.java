package Controller;

import Entities.SessionManager;
import Entities.User;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class BaseFrontController {

    @FXML
    private ImageView bg_pic;
    @FXML
    private Button logout;
    @FXML
    private ImageView pp_view;
    @FXML
    private TextField search_tf;



    public void Commande(ActionEvent event) {
        loadFXML("/Commande/CommandeV.fxml", event);
    }

    public void Blogs(ActionEvent event) {
        loadFXML("/Post/Post-front.fxml", event);
    }


    private void loadFXML(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Error loading the new scene: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Retrieve the current user ID from the session manager
        String userId = SessionManager.getInstance().getUserId();

        // Use the user ID to fetch user details from the database
        UserService userService = new UserService();
        User user = userService.afficher(userId);

        // Populate the labels with user details
        if (user != null) {
            try {
                // Set the image to the ImageView
                Image image1 = new Image("file:C:/Users/bouaz/PREVIOUS/src/main/java/image/logo.png");
                bg_pic.setImage(image1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                // Set the image to the ImageView
                Image image2 = new Image("file:C:/Users/bouaz/PREVIOUS/src/main/java/image/Untitled design.png");
                bg_pic.setImage(image2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String photoPath = user.getPhoto();
            if (photoPath != null && !photoPath.isEmpty()) {
                try {
                    // Set the image to the ImageView
                    Image image = new Image("file:" + photoPath);
                    pp_view.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void gotoprofile() throws IOException {
        Stage stage = (Stage) pp_view.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("/User/UpdateProfile.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User SignIn");
        stage.show();
    }
    @FXML
    public void logout(ActionEvent event) {
        System.out.println("begin basefront");
        SessionManager.getInstance().cleanUserSessionAdmin();
        System.out.println("session basefront");
        try {
            System.out.println("session basefront");
            Node sourceNode = (Node) logout;
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.close();
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/LogIn.fxml")));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void Exercice(ActionEvent event) {
        loadFXML("/Exercice/ExerciceV.fxml", event);
    }
}
