package Controller;
import Entities.panier;
import Entities.product;
import Service.panierService;
import Service.productService;
import com.example.projectpi.HelloApplication;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public class panierfront extends Application {

    private PDDocument document;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/paniers/basefrontp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @FXML
    public ListView<product> cartListView;

    private ObservableList<product> cartProducts = FXCollections.observableArrayList();
    @FXML
    private Spinner<?> spinner;
    @FXML
    private Button pay;
    @FXML
    private TextField pricetot;
    @FXML
    private Button remove;
    @FXML
    private Button valider;
    @FXML
    private Label shop;
    @FXML
    private Label cartdetails;

    @FXML
    void cartdetails(MouseEvent event) {
       // generatePDF(null);
       generatePDF(null);


    }
    @FXML
    void generatePDF(ActionEvent event) {
        try {
            // Créer un document PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Définir la police et la taille du texte pour le titre
            PDFont titleFont = PDType1Font.HELVETICA_BOLD;
            float titleFontSize = 18;

            // Définir la police et la taille du texte pour les détails du produit
            PDFont font = PDType1Font.HELVETICA;
            float fontSize = 12;

            // Définir les marges et la position initiale du texte
            float margin = 50;
            float yPosition = page.getMediaBox().getHeight() - margin;

            // Ajouter le titre
            addText(contentStream, "Liste des Achats", titleFont, titleFontSize, margin, yPosition);
            yPosition -= titleFontSize + 20; // Espacement après le titre

            // Ajouter la date et l'heure actuelles
            Date currentDate = new Date();
            addText(contentStream, "Date: " + currentDate.toString(), font, fontSize, margin, yPosition);
            yPosition -= fontSize * 2; // Espacement après la date

            // Ajouter un message de remerciement en anglais
            addText(contentStream, "Thank you for your trust and placing an order.", font, fontSize, margin, yPosition);
            yPosition -= fontSize * 2; // Espacement après le message de remerciement

           /* // Ajouter le prix total
            addText(contentStream, "Total Price: " + pricetot + " TND", font, fontSize, margin, yPosition);
            yPosition -= fontSize * 2; // Espacement après le prix total*/

            // Ajouter les détails des produits au PDF
            List<product> productList = new ArrayList<>(cartListView.getItems());
            for (product p : productList) {
                // Ajouter le nom du produit
                addText(contentStream, "Nom du produit: " + p.getNom(), font, fontSize, margin, yPosition);
                yPosition -= fontSize * 2; // Espacement entre les lignes

                // Ajouter la description du produit
                addText(contentStream, "Description: " + p.getDescription(), font, fontSize, margin, yPosition);
                yPosition -= fontSize * 2; // Espacement entre les lignes

                // Ajouter le prix du produit
                addText(contentStream, "Prix: " + p.getPrix(), font, fontSize, margin, yPosition);
                yPosition -= fontSize * 2; // Espacement entre les lignes

                // Ajouter une ligne horizontale pour séparer chaque groupe de nom, description et prix
                addHorizontalLine(contentStream, margin, yPosition - fontSize);

                // Déplacer la position vers le bas pour le prochain produit
                yPosition -= fontSize * 2; // Espacement entre les groupes de produits
            }

            // Fermer le contenu du flux et enregistrer le document
            contentStream.close();
            document.save("panier_details.pdf");
            document.close();

            // Afficher un message indiquant que le PDF a été généré avec succès
            System.out.println("PDF généré avec succès");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void addText(PDPageContentStream contentStream, String text, PDFont font, float fontSize, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private void addLogo(PDPageContentStream contentStream, String imagePath, float x, float y, float width, float height) throws IOException {
        try (InputStream in = new FileInputStream(imagePath)) {
            // Charger l'image et l'ajouter à la page du document
            PDImageXObject logo = PDImageXObject.createFromByteArray(document, IOUtils.toByteArray(in), "logo");

            // Dessiner l'image en spécifiant la position (x, y), la largeur et la hauteur
            contentStream.drawImage(logo, x, y, width, height);
        }
    }





    // Méthode pour ajouter une ligne horizontale à la page PDF
    private void addHorizontalLine(PDPageContentStream contentStream, float x, float y) throws IOException {
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + 400, y); // Ajustez la longueur de la ligne selon vos besoins
        contentStream.stroke();
    }



    @FXML
    void pay(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paniers/payment.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void viewshop(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/products/basefront.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        try {
            panierService ez = new panierService();
            List<product> productList = ez.afficherList().getproductList();

            // Clear the existing items in the cartListView
            cartListView.getItems().clear();

            // Set the custom cell factory for the ListView
            cartListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(product item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Set the text to display only the name, description, and price of the product
                        setText("Name: " + item.getNom() + "\nDescription: " + item.getDescription() + "\nPrice: " + item.getPrix());
                    }
                }
            });

            // Add each product from the productList to the cartListView
            cartListView.getItems().addAll(productList);

            // Calculate total price
            calculateTotalPrice();

            System.out.println("Products added to cartListView");
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }

        // Ajouter un gestionnaire d'événements sur le bouton Remove
        remove.setOnAction(event -> {
            // Récupérer l'élément sélectionné dans le ListView
            product selectedItem = cartListView.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {

                    // Supprimer l'élément sélectionné de la liste des produits du panier
                    cartListView.getItems().remove(selectedItem);

                    // Recalculer le prix total
                    calculateTotalPrice();

            } else {
                // Afficher un message d'erreur si aucun élément n'est sélectionné
                System.out.println("No item selected to remove.");
            }
        });
        valider.setOnAction(event -> {
            // Récupérer le numéro de téléphone du destinataire du SMS
            String phoneNumber = ""; // Remplacez par le numéro de téléphone du destinataire

            // Récupérer le corps du SMS à envoyer
            String messageBody = "Votre commande a été validée avec succès.";

            // Vos identifiants Twilio
            String accountSid = "";
            String authToken = "";

            // Initialiser le client Twilio
            Twilio.init(accountSid, authToken);

            // Envoyer le SMS
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber(phoneNumber),
                            new com.twilio.type.PhoneNumber(""),
                            messageBody)
                    .create();

            // Afficher un message indiquant que le SMS a été envoyé avec succès
            System.out.println("SMS envoyé avec succès: " + message.getSid());
        });

    }

    private void calculateTotalPrice() {
        try {
            panierService ez = new panierService();
            // Get the list of products in the cart
            List<product> productList = new ArrayList<>(cartListView.getItems());

            // Initialize total price
            float totalPrice = 0;

            // Iterate over each product in the cart and sum up their prices
            for (product p : productList) {
                totalPrice += p.getPrix();
            }

            // Display the total price in the pricetot TextField
            pricetot.setText(String.valueOf(totalPrice));
        } catch (SQLException e) {
            System.out.println("Error calculating total price: " + e.getMessage());
        }
    }

    private void processPayment() {
        try {
// Set your secret key here
            Stripe.apiKey = "";

// Create a PaymentIntent with other payment details
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(1000L) // Amount in cents (e.g., $10.00)
                    .setCurrency("TND")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

// If the payment was successful, display a success message
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
        } catch (StripeException e) {
// If there was an error processing the payment, display the error message
            System.out.println("Payment failed. Error: " + e.getMessage());
        }
    }
}
