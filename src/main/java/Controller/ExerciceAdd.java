package Controller;

import Entities.Exercice;
import Service.ExerciceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
import Entities.SessionManager;
import Entities.User;
import Service.UserService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import javafx.stage.FileChooser;
import java.io.File;

import javafx.util.Duration;
import org.controlsfx.control.Notifications;
public class ExerciceAdd {

    @FXML
    private Label nomE;
    @FXML
    private Label desE;
    @FXML
    private Label mcE;
    @FXML
    private Label ndE;
    @FXML
    private Label imgE;
    @FXML
    private Label gifE;



    @FXML
    private TextField nomA;
    @FXML
    private TextArea desA;
    @FXML
    private ChoiceBox<String> mcA;
    @FXML
    private ChoiceBox<String> ndA;
    @FXML
    private TextField imgA;
    @FXML
    private TextField gifA;

    @FXML
    private Button ib;
    @FXML
    private Button gb;
    private Stage primaryStage;




    // Méthode appelée lorsque le bouton est cliqué
    public void EX() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/Exercice-front.fxml"));
            primaryStage=(Stage)nomA.getScene().getWindow();
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
    public void initialize() {


        // Use the user ID to fetch user details from the database


            // Action à effectuer lors du clic sur le bouton
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
                        String destinationDirectory = "Front/images/exo/";
                        String fileName = selectedFile.getName();

                        // Obtenir le répertoire de destination dans les ressources
                        Path destinationPath = Paths.get("src/main/resources", destinationDirectory);

                        // Créer le chemin complet du fichier de destination
                        Path destinationFilePath = destinationPath.resolve(fileName);

                        // Copier le fichier sélectionné vers le répertoire de destination dans les ressources
                        Files.copy(selectedFile.toPath(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

                        // Mettre à jour le chemin de l'image dans imgA
                        String newImagePath = destinationDirectory + fileName;
                        imgA.setText(newImagePath);

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


            gb.setOnAction(e -> {
                // Création du sélecteur de fichier
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choisir une Gif");

                // Filtrer les types de fichiers
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Gifs", "*.gif"),
                        new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
                );

                // Afficher la boîte de dialogue pour sélectionner un fichier
                File selectedFilee = fileChooser.showOpenDialog(primaryStage);
                // gifA=selectedFilee.getAbsolutePath()
                // Vérifier si un fichier a été sélectionné
                if (selectedFilee != null) {
                    try {
                        // Définir le répertoire de destination dans les ressources
                        String destinationDirectory = "Front/images/exo/gif/";
                        String fileName = selectedFilee.getName();

                        // Obtenir le répertoire de destination dans les ressources
                        Path destinationPath = Paths.get("src/main/resources", destinationDirectory);

                        // Créer le chemin complet du fichier de destination
                        Path destinationFilePath = destinationPath.resolve(fileName);

                        // Copier le fichier sélectionné vers le répertoire de destination dans les ressources
                        Files.copy(selectedFilee.toPath(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

                        // Mettre à jour le chemin de l'image dans imgA
                        String newImagePath = destinationDirectory + fileName;
                        gifA.setText(newImagePath);

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


            ObservableList<String> valeurs = FXCollections.observableArrayList("Pectoraux", "Epaules", "Biceps", "Triceps", "Abdos", "Dos", "Quadriceps", "Ischio-jambiers", "Fessiers", "Mollets");
            mcA.setItems(valeurs);
            ObservableList<String> valeurss = FXCollections.observableArrayList("Facile", "Moyen", "Difficile");
            ndA.setItems(valeurss);

    }

    @FXML
    protected void AjouterEX() {
        ExerciceService es = new ExerciceService();
boolean test=true;
        // Vérifier si tous les champs de texte sont remplis
        if (nomA.getText().isEmpty()) {
            test = false;
            nomE.setText("vide");
        } else if (!nomA.getText().matches("^[^0-9]*$")) {
            test = false;
            nomE.setText("Le nom ne doit pas contenir de chiffres");
        } else {
            nomE.setText("");
        }


        if (desA.getText().isEmpty()) {
            test=false;
            desE.setText("vide");
        }else {desE.setText("");}


        if (mcA.getValue() == null ||
                (!mcA.getValue().equals("Pectoraux") &&
                        !mcA.getValue().equals("Epaules") &&
                        !mcA.getValue().equals("Biceps") &&
                        !mcA.getValue().equals("Triceps") &&
                        !mcA.getValue().equals("Abdos") &&
                        !mcA.getValue().equals("Dos") &&
                        !mcA.getValue().equals("Quadriceps") &&
                        !mcA.getValue().equals("Ischio-jambiers") &&
                        !mcA.getValue().equals("Fessiers") &&
                        !mcA.getValue().equals("Mollets"))) {
            test = false;
            mcE.setText("Veuillez choisir parmi Pectoraux, Epaules, Biceps, Triceps, Abdos, Dos, Quadriceps, Ischio-jambiers, Fessiers ou Mollets");
        } else {
            mcE.setText("");
        }


        if (ndA.getValue() == null || ndA.getValue().isEmpty() ||
                (!ndA.getValue().equals("Facile") &&
                        !ndA.getValue().equals("Moyen") &&
                        !ndA.getValue().equals("Difficile"))) {
            test = false;
            ndE.setText("Veuillez choisir parmi facile, moyen ou difficile");
        } else {
            ndE.setText("");
        }

        if (imgA.getText().isEmpty()) {
            test=false;
            imgE.setText("vide");
        }else {imgE.setText("");}


        if (gifA.getText().isEmpty()) {
            test=false;
            gifE.setText("vide");
        }else {gifE.setText("");}


           if (test){
            String a;
            if(ndA.getValue().equals("Facile")) a="1";
            else if (ndA.getValue().equals("Moyen")) a="2";
             else a="3";

               System.out.println(imgA.getText());
            Exercice ex = new Exercice(nomA.getText(), desA.getText(), mcA.getValue(),
                    a, imgA.getText(), gifA.getText());
            try{
            es.add(ex);
                /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("L'exercice a été a jouté avec succées !!");
                alert.showAndWait();*/
                EX();
                ImageView commentaireIcon = new ImageView(new Image(getClass().getResourceAsStream("/Front/images/exo/ez.png")));
                commentaireIcon.setFitHeight(50);
                commentaireIcon.setFitWidth(50);
                Notifications.create()
                        .title("Succès")
                        .text("L'exercice a été ajouté avec succès !!")
                        .graphic(commentaireIcon) // No custom image
                        .hideAfter(Duration.seconds(7)) // Notification disappears after 5 seconds
                        .position(Pos.BOTTOM_RIGHT) // Position the notification on the screen
                        .darkStyle() // Use a dark style for the notification
                        .onAction(event -> {
                            System.out.println("Notification cliquée !");
                            // Add any action you want to perform when the notification is clicked
                        })
                        .show(); // Display the notification

// Play a sound effect
                java.awt.Toolkit.getDefaultToolkit().beep();


           } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        } else {

            System.out.println("Veuillez remplir tous les champs.");
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Echec");
               alert.setContentText("Echec de l'ajout de l'exercice !!");
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

}