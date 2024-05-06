package Controller;

import Entities.SessionManager;
import Service.panierService;
import com.example.projectpi.HelloApplication;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import Entities.product;
import Service.productService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import org.controlsfx.control.Notifications;

public class cardController  {


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

    @FXML
    private Spinner<?> spinner;
    private panierfront panierfront;
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private Button addtocart;

    private product p;

    String userId = SessionManager.getInstance().getUserId();
    public void setpanierfront(panierfront panierfront) {
        this.panierfront = panierfront;
    }
    public void setData(product product) {
        // Charger l'image  à l'ImageView
        String imageName = "C:/Users/bouaz/PREVIOUS/src/main/java/image/download.jpg";//product.getImage();
        Image image = new Image(imageName);

        // Attribuer l'image chargée à l'ImageView
        imagep.setImage(image);

        p= product;
        // Attribuer le nom, la description et le prix du produit aux labels correspondants
        nom.setText(product.getNom());
        description.setText(product.getDescription());
        prix.setText(String.valueOf(product.getPrix()));

    }
    @FXML
    void addtocart(ActionEvent event) {
        try {
            // Création d'un nouveau produit (remplacez ces lignes par la logique appropriée pour créer un nouveau produit)
            String productName = p.getNom();
            String productDescription = p.getDescription();
            float productPrice = p.getPrix(); // Prix du produit
            try {

                panierService ez=new panierService();

                ez.add(p,userId);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            // Créer un nouvel objet product
            product newProduct = new product(productName, productDescription, productPrice);
            Notifications.create()
                    .title("Produit ajouté")
                    .text("Le produit " + productName + " a été ajouté à votre panier.")
                    .showInformation();


            // Mettre à jour l'affichage du panier

        } catch (NullPointerException e) {
            e.printStackTrace(); // Ou autre traitement approprié
        }
    }


   /* public void initialize(URL location, ResourceBundle resources) {
        // Obtenez une instance du contrôleur de panier
        panierfront panierfront= (panierfront) rootAnchorPane.getScene().getUserData();

        // Vérifiez si le contrôleur de panier n'est pas null avant de mettre à jour la liste de produits
        if (panierfront != null) {
            panierfront.updateCartListView(cartProducts);
        }
    }*/

}