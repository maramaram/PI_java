package Controller;

import com.example.projectpi.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import Entities.product;
import Service.productService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class cardController extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/products/card.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @FXML
    private Button addtocart;

    @FXML
    private HBox box;

    @FXML
    private Label description;

    @FXML
    private ImageView imagep;

    @FXML
    private Label nom;

    @FXML
    private Label prix;
    public void setData(product product) {
        // Charger l'image  à l'ImageView
        String imageName = product.getImage();
        Image image = new Image(imageName);

        // Attribuer l'image chargée à l'ImageView
        imagep.setImage(image);


        // Attribuer le nom, la description et le prix du produit aux labels correspondants
        nom.setText(product.getNom());
        description.setText(product.getDescription());
        prix.setText(String.valueOf(product.getPrix()));
    }

}
