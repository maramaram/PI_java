package Controller;

import Entities.Post;
import Service.PostService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import javafx.stage.FileChooser;
import java.io.File;

import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class PostAddV {

    @FXML
    private Label titleE;
    @FXML
    private Label contenuE;

    @FXML
    private Label imageE;

    @FXML
    private Button ib;





    @FXML
    private TextField titleA;
    @FXML
    private TextArea contenuA;

    @FXML
    private TextField imageA;


    private Stage primaryStage;



    @FXML
    public void initialize() {
        ib.setOnAction(e -> {
            // Création du sélecteur de fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");

            // Filtrer les types de fichiers
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.bmp", "*.jpeg"),
                    new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
            );

            // Afficher la boîte de dialogue pour sélectionner un fichier
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            // Vérifier si un fichier a été sélectionné
            if (selectedFile != null) {
                try {
                    // Définir le répertoire de destination dans les ressources
                    String destinationDirectory = "Front/images/blog/";
                    String fileName = selectedFile.getName();

                    // Obtenir le répertoire de destination dans les ressources
                    Path destinationPath = Paths.get("src/main/resources", destinationDirectory);

                    // Créer le chemin complet du fichier de destination
                    Path destinationFilePath = destinationPath.resolve(fileName);

                    // Copier le fichier sélectionné vers le répertoire de destination dans les ressources
                    Files.copy(selectedFile.toPath(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

                    // Mettre à jour le chemin de l'image dans imgA
                    String newImagePath = destinationDirectory + fileName;
                    imageA.setText(newImagePath);

                    // Afficher un message de succès
                    System.out.println("Fichier téléchargé avec succès dans les ressources. Nouveau chemin : " + newImagePath);
                } catch (IOException ex) {
                    // Gérer les exceptions en cas d'erreur de téléchargement
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Aucun fichier sélectionné.");
            }
        });

        }


    // Méthode appelée lorsque le bouton est cliqué
    public void EX() {

        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Post/PostV.fxml"));
            primaryStage=(Stage)titleA.getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }


    @FXML
    protected void AjouterEX() {
        PostService es = new PostService();
        boolean test=true;
        // Vérifier si tous les champs de texte sont remplis
        if (titleA.getText().isEmpty() ){
            test=false;
            titleE.setText("vide");
        }else {titleE.setText("");}


        if (contenuA.getText().isEmpty()) {
            test=false;
            contenuE.setText("vide");
        }else {contenuE.setText("");}

        if (imageA.getText().isEmpty()) {
            test=false;
            imageE.setText("vide");
        }else {imageE.setText("");}


        if (test){
            List<Post> l=new ArrayList<>();
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(date);

// Now, create the Post object using the parsed Date object
            Post ex = new Post(

                    titleA.getText(),
                    contenuA.getText(),
                    date,
                    imageA.getText(),
                    0,
                    1,
                    new ArrayList<>()
            );
            try{
                es.add(ex);
/*                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("Le post a été a jouté avec succées !!");
                alert.showAndWait();*/
                ImageView commentaireIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/comment.png")));
                commentaireIcon.setFitHeight(50);
                commentaireIcon.setFitWidth(50);
                Notifications.create()
                        .title("Succès")
                        .text("Le post a été ajouté avec succès !!")
                        .graphic(commentaireIcon) // No custom image
                        .hideAfter(Duration.seconds(5)) // Notification disappears after 5 seconds
                        .position(Pos.BOTTOM_RIGHT) // Position the notification on the screen
                        .darkStyle() // Use a dark style for the notification
                        .onAction(event -> {
                            System.out.println("Notification cliquée !");
                            // Add any action you want to perform when the notification is clicked
                        })
                        .show(); // Display the notification

// Play a sound effect
                java.awt.Toolkit.getDefaultToolkit().beep();

                EX();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else {

            System.out.println("Veuillez remplir tous les champs.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Echec");
            alert.setContentText("Echec de l'ajout de post !!");
            alert.showAndWait();
        }



    }



    public void Exercices(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/Exercice-front.fxml"));
            primaryStage=(Stage)((Node)event.getSource()).getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }


    public void Defis(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/Defi-front.fxml"));
            primaryStage=(Stage)((Node)event.getSource()).getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }

    @FXML
    public void Blogs(MouseEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène
            Parent root = FXMLLoader.load(getClass().getResource("/Post/PostV.fxml"));

            // Obtenir la scène à partir de l'événement source
            primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la nouvelle scène à la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }




}