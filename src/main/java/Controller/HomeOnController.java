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


        // Use the user ID to fetch user details from the database
        UserService userService = new UserService();
        User user = userService.afficher(userId);
        if (user != null) {
              this.pp1.setImage(new Image(this.getClass().getResourceAsStream("/images/pp1.jpg")));
              this.pp2.setImage(new Image(this.getClass().getResourceAsStream("/images/pp2.jpg")));
              this.pp3.setImage(new Image(this.getClass().getResourceAsStream("/images/pp3.jpg")));
              this.pp4.setImage(new Image(this.getClass().getResourceAsStream("/images/pp4.jpg")));
              this.pp5.setImage(new Image(this.getClass().getResourceAsStream("/images/pp5.jpg")));
              this.pp6.setImage(new Image(this.getClass().getResourceAsStream("/images/pp6.jpg")));
              this.pp7.setImage(new Image(this.getClass().getResourceAsStream("/images/pp7.jpg")));
            this.pp8.setImage(new Image(this.getClass().getResourceAsStream("/images/pp8.jpg")));
            this.pp9.setImage(new Image(this.getClass().getResourceAsStream("/images/logo.png")));

            // Optionally, show an alert or fallback image


            firstname.setText(user.getPrenom());
            lastname.setText(user.getNom());

        }
        }
    }

    // Use the user ID to fetch user details from the database




