package Controller;

import Entities.SessionManager;
import Entities.User;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeOnController extends BaseFrontController implements Initializable {
    @FXML
    private Label firstname;

    @FXML
    private Label lastname;
    String userId = SessionManager.getInstance().getUserId();
    @FXML
    private ImageView pp1;

    @FXML
    private ImageView pp2;

    @FXML
    private ImageView pp3;

    @FXML
    private ImageView pp4;

    @FXML
    private ImageView pp5;

    @FXML
    private ImageView pp6;

    @FXML
    private ImageView pp7;

    @FXML
    private ImageView pp8;

    @FXML
    private ImageView pp9;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

/*
        // Use the user ID to fetch user details from the database
        UserService userService = new UserService();
        User user = userService.afficher(userId);
        if (user != null) {
            try {
                // Convert the file path to a URL
                File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/pp1.jpg");
                String imageUrl = file.toURI().toURL().toString();
                // Create an Image object from the URL
                Image image = new Image(imageUrl);
                // Set the image to the ImageView
                pp1.setImage(image);
            } catch (MalformedURLException e) {
                // Handle invalid URL exception
                e.printStackTrace();
                // Optionally, show an alert or fallback image
            }
            try {
                // Convert the file path to a URL
                File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/pp2.jpg");
                String imageUrl = file.toURI().toURL().toString();
                // Create an Image object from the URL
                Image image = new Image(imageUrl);
                // Set the image to the ImageView
                pp2.setImage(image);
            } catch (MalformedURLException e) {
                // Handle invalid URL exception
                e.printStackTrace();
                // Optionally, show an alert or fallback image
            }
            try {
                // Convert the file path to a URL
                File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/pp3.jpg");
                String imageUrl = file.toURI().toURL().toString();
                // Create an Image object from the URL
                Image image = new Image(imageUrl);
                // Set the image to the ImageView
                pp3.setImage(image);
            } catch (MalformedURLException e) {
                // Handle invalid URL exception
                e.printStackTrace();
                // Optionally, show an alert or fallback image
            }
            try {
                // Convert the file path to a URL
                File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/pp4.jpg");
                String imageUrl = file.toURI().toURL().toString();
                // Create an Image object from the URL
                Image image = new Image(imageUrl);
                // Set the image to the ImageView
                pp4.setImage(image);
            } catch (MalformedURLException e) {
                // Handle invalid URL exception
                e.printStackTrace();
                // Optionally, show an alert or fallback image
            }
            try {
                // Convert the file path to a URL
                File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/pp5.jpg");
                String imageUrl = file.toURI().toURL().toString();
                // Create an Image object from the URL
                Image image = new Image(imageUrl);
                // Set the image to the ImageView
                pp5.setImage(image);
            } catch (MalformedURLException e) {
                // Handle invalid URL exception
                e.printStackTrace();
                // Optionally, show an alert or fallback image
            }
            try {
                // Convert the file path to a URL
                File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/pp6.jpg");
                String imageUrl = file.toURI().toURL().toString();
                // Create an Image object from the URL
                Image image = new Image(imageUrl);
                // Set the image to the ImageView
                pp6.setImage(image);
            } catch (MalformedURLException e) {
                // Handle invalid URL exception
                e.printStackTrace();
                // Optionally, show an alert or fallback image
            }
            try {
                // Convert the file path to a URL
                File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/pp7.jpg");
                String imageUrl = file.toURI().toURL().toString();
                // Create an Image object from the URL
                Image image = new Image(imageUrl);
                // Set the image to the ImageView
                pp7.setImage(image);
            } catch (MalformedURLException e) {
                // Handle invalid URL exception
                e.printStackTrace();
                // Optionally, show an alert or fallback image
            }
            try {
                // Convert the file path to a URL
                File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/pp8.jpg");
                String imageUrl = file.toURI().toURL().toString();
                // Create an Image object from the URL
                Image image = new Image(imageUrl);
                // Set the image to the ImageView
                pp8.setImage(image);
            } catch (MalformedURLException e) {
                // Handle invalid URL exception
                e.printStackTrace();
                // Optionally, show an alert or fallback image
            }
            try {
                // Convert the file path to a URL
                File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/logo.png");
                String imageUrl = file.toURI().toURL().toString();
                // Create an Image object from the URL
                Image image = new Image(imageUrl);
                // Set the image to the ImageView
                pp9.setImage(image);
            } catch (MalformedURLException e) {
                // Handle invalid URL exception
                e.printStackTrace();
                // Optionally, show an alert or fallback image
            }
            firstname.setText(user.getPrenom());
            lastname.setText(user.getNom());

        }*/
        }
    }

    // Use the user ID to fetch user details from the database




