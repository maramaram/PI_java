package Controller;

import Entities.Exercice;
import Service.ExerciceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;


public class ExerciceUpdate {




    @FXML
    private Label nomEE;
    @FXML
    private Label desEE;
    @FXML
    private Label mcEE;
    @FXML
    private Label ndEE;
    @FXML
    private Label imgEE;
    @FXML
    private Label gifEE;




    @FXML
    private TextField idM;
    @FXML
    private TextField nomM;
    @FXML
    private TextArea desM;
    @FXML
    private ChoiceBox<String> mcM;
    @FXML
    private ChoiceBox<String> ndM;
    @FXML
    private TextField imgM;
    @FXML
    private TextField gifM;
    @FXML
    private Button ib;
    @FXML
    private Button gb;

    public void setIdM(String idM) {
        this.idM.setText(idM);
    }

    public void setNomM(String nomM) {
        this.nomM.setText(nomM);
    }

    public void setDesM(String desM) {
        this.desM.setText(desM);
    }

    public void setMcM(String mcM) {
        //initialize();
        this.mcM.setValue(mcM);
    }

    public void setNdM(String ndM) {
        if (ndM.equals("1"))
        this.ndM.setValue("Facile");
        else if (ndM.equals("2"))
            this.ndM.setValue("Moyen");
        else this.ndM.setValue("Difficile");
    }

    public void setImgM(String imgM) {
        this.imgM.setText(imgM);
    }

    public void setGifM(String gifM) {
         this.gifM.setText(gifM);
    }

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
                    imgM.setText(newImagePath);

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
                    gifM.setText(newImagePath);

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



        ObservableList<String> valeurs = FXCollections.observableArrayList("Pectoraux", "Epaules", "Biceps", "Triceps", "Abdos", "Dos", "Quadriceps", "Ischio-jambiers", "Fessiers" , "Mollets");
        mcM.setItems(valeurs);
        ObservableList<String> valeurss = FXCollections.observableArrayList("Facile", "Moyen", "Difficile");
        ndM.setItems(valeurss);
    }
    public void EX() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/Exercice-front.fxml"));
            primaryStage=(Stage)nomM.getScene().getWindow();
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
    protected void ModifierEX() {
        ExerciceService es = new ExerciceService();
        boolean test=true;
        // Vérifier si tous les champs de texte sont remplis
        if (nomM.getText().isEmpty()) {
            test = false;
            nomEE.setText("vide");
        } else if (!nomM.getText().matches("^[^0-9]*$")) {
            test = false;
            nomEE.setText("Le nom ne doit pas contenir de chiffres");
        } else {
            nomEE.setText("");
        }

        if (desM.getText().isEmpty()) {
            test=false;
            desEE.setText("vide");
        }else {desEE.setText("");}


        if (mcM.getValue() == null ||
                (!mcM.getValue().equals("Pectoraux") &&
                        !mcM.getValue().equals("Epaules") &&
                        !mcM.getValue().equals("Biceps") &&
                        !mcM.getValue().equals("Triceps") &&
                        !mcM.getValue().equals("Abdos") &&
                        !mcM.getValue().equals("Dos") &&
                        !mcM.getValue().equals("Quadriceps") &&
                        !mcM.getValue().equals("Ischio-jambiers") &&
                        !mcM.getValue().equals("Fessiers") &&
                        !mcM.getValue().equals("Mollets"))) {
            test = false;
            mcEE.setText("Veuillez choisir parmi Pectoraux, Epaules, Biceps, Triceps, Abdos, Dos, Quadriceps, Ischio-jambiers, Fessiers ou Mollets");
        } else {
            mcEE.setText("");
        }


        if (ndM.getValue() == null || ndM.getValue().isEmpty() ||( !ndM.getValue().equals("Facile") &&
                !ndM.getValue().equals("Moyen") &&
                !ndM.getValue().equals("Difficile")) ) {
            test=false;
            ndEE.setText("Veuillez choisir parmi facile , moyen ou difficile");
        }else {ndEE.setText("");}


        if (imgM.getText().isEmpty()) {
            test=false;
            imgEE.setText("vide");
        }else {imgEE.setText("");}


        if (gifM.getText().isEmpty()) {
            test=false;
            gifEE.setText("vide");
        }else {gifEE.setText("");}


        if (test){
            String a;
            if(ndM.getValue().equals("Facile")) a="1";
            else if (ndM.getValue().equals("Moyen")) a="2";
            else a="3";
            Exercice ex = new Exercice(Integer.parseInt(idM.getText()), nomM.getText(), desM.getText(), mcM.getValue(),
                    a, imgM.getText(), gifM.getText());
            try{
                es.modifier(ex);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("L'exercice a été a Modifié avec succées !!");
                alert.showAndWait();
                EX();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else {

            System.out.println("Veuillez remplir tous les champs.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Echec");
            alert.setContentText("Echec de la modification de l'exercice !!");
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