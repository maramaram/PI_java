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

public class ClientPannelController {
    @FXML
    private Label logout_btn;




    @FXML
    private ImageView pp_view;

    @FXML
    private Label Birthdate;

    @FXML
    private Label adresse;

    @FXML
    private Label email;

    @FXML
    private Label firstname;

    @FXML
    private Label lastname;
    @FXML
    private Button update_btn;

    @FXML
    private Label phonenumber;
    private UserService userService;
    @FXML
    private ImageView pp_view1;
    @FXML
    private Button NewPwd_btn;
    String imagePath="";
    public ClientPannelController() {
        this.userService = new UserService();
    }
    @FXML
    void actionNewPwd_btn(ActionEvent eventAction) throws IOException {
        Stage stage = (Stage) update_btn.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/EnterPassword.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User SignIn");
        stage.show();
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
            firstname.setText(user.getNom());
            lastname.setText(user.getPrenom());
            email.setText(user.getEmail());
            phonenumber.setText(user.getNum_tel());
            Birthdate.setText(user.getDate_N().toString()); // Assuming Birthdate is a LocalDate
            adresse.setText(user.getAdress());

            String photoPath = user.getPhoto();
            if (photoPath != null && !photoPath.isEmpty()) {
                try {
                    File file = new File(photoPath);
                    String imageUrl = file.toURI().toURL().toString();
                    Image image = new Image(imageUrl);
                   pp_view1.setImage(image);
                } catch (MalformedURLException e) {
                    // Handle invalid URL exception
                    e.printStackTrace();
                    // Show an alert or fallback image
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


    public void showUpdateStage(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) update_btn.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/UpdateProfile.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User SignIn");
        stage.show();
    }
}
