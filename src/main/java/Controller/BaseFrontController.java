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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class BaseFrontController {


    @FXML
    private ImageView pp_view;



        @FXML
        public void Commande() {
            loadFXML("/Commande/CommandeV.fxml");
        }
        @FXML
        public void Panier() {loadFXML("/paniers/basefrontp.fxml");}
        @FXML
        public void Blogs() {
            loadFXML("/Post/PostV.fxml");
        }
        @FXML
        public void Exercice() {
            loadFXML("/Exercice/ExerciceV.fxml");
        }
        @FXML
        public void Defi() {
            loadFXML("/Defi/DefiV.fxml");
        }
    @FXML
    public void Shop() {loadFXML("/products/basefront.fxml");}
        @FXML
        public void Logout() {loadFXML("/User/LogIn.fxml");}

        private void loadFXML(String fxmlPath) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage)pp_view.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.println("Error loading FXML: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @FXML
        public void gotoprofile() {
            Stage stage = (Stage) pp_view.getScene().getWindow();
            stage.close();
            loadFXML("/User/UpdateProfile.fxml");
        }



        @FXML
        public void initialize() {
            String userId = SessionManager.getInstance().getUserId();
            UserService userService = new UserService();
            User user = userService.afficher(userId);

            if (user != null) {

                String photoPath = user.getPhoto();

                try {
                    Image image2 = new Image(getClass().getResourceAsStream("/"+photoPath));
                    pp_view.setImage(image2);
                } catch (Exception e) {
                    System.out.println("Error loading profile image: " + e.getMessage());
                    e.printStackTrace();
                }


                if (photoPath != null && !photoPath.isEmpty()) {

                        Image image = new Image(getClass().getResourceAsStream("/"+photoPath));
                        pp_view.setImage(image);
                    
                }
            }
        }


}


