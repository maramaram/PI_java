
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

        public class BaseFrontController {



        @FXML
        private Label logout_btn;


            @FXML
            private ImageView bg_pic;


        @FXML
        private ImageView pp_view;
        public BaseFrontController() {

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
                    // Convert the file path to a URL
                    File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/logo.png");
                    String imageUrl = file.toURI().toURL().toString();
                    // Create an Image object from the URL
                    Image image = new Image(imageUrl);
                    // Set the image to the ImageView
                    bg_pic.setImage(image);
                } catch (MalformedURLException e) {
                    // Handle invalid URL exception
                    e.printStackTrace();
                    // Optionally, show an alert or fallback image
                }
                try {
                    // Convert the file path to a URL
                    File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/Untitled design.png");
                    String imageUrl = file.toURI().toURL().toString();
                    // Create an Image object from the URL
                    Image image = new Image(imageUrl);
                    // Set the image to the ImageView
                    bg_pic.setImage(image);
                } catch (MalformedURLException e) {
                    // Handle invalid URL exception
                    e.printStackTrace();
                    // Optionally, show an alert or fallback image
                }
                String photoPath = user.getPhoto();
                if (photoPath != null && !photoPath.isEmpty()) {
                    try {
                        // Convert the file path to a URL
                        File file = new File(photoPath);
                        String imageUrl = file.toURI().toURL().toString();
                        // Create an Image object from the URL
                        Image image = new Image(imageUrl);
                        // Set the image to the ImageView
                        pp_view.setImage(image);
                    } catch (MalformedURLException e) {
                        // Handle invalid URL exception
                        e.printStackTrace();
                        // Optionally, show an alert or fallback image
                    }
                }

            }}

        @FXML
        void logout_click(MouseEvent event) {
            SessionManager.getInstance().cleanUserSessionAdmin();
            try {
                Node sourceNode = (Node) logout_btn;
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


            @FXML
            void gotoprofile(MouseEvent event) throws IOException {
                Stage stage = (Stage) pp_view.getScene().getWindow();
                stage.close();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/UpdateProfile.fxml")));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("User SignIn");
                stage.show();
            }

    }


