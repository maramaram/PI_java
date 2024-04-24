package Controller;

import Entities.Defi;
import Entities.Exercice;
import Service.DefiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DefiV {


    private Stage primaryStage;

    @FXML
    private VBox vbox;
    @FXML
    private TextField search ;

    @FXML
    public void initialize() {

        AfficherEX(); // Appeler la méthode pour afficher les données

    }




    @FXML
    protected void AfficherEX() {
        DefiService es = new DefiService();
        try {
            List<Defi> l = es.afficherList();

            // Effacer les données existantes de la VBox
            vbox.getChildren().clear();

            // Parcourir la liste des defis
            for (int i = 0; i < l.size(); i += 3) {
                // Créer une HBox pour chaque groupe de quatre defis
                HBox hbox = new HBox();
                hbox.setSpacing(20); // Ajouter plus d'espace entre les éléments de la HBox

                // Ajouter les quatre éléments de l'defi à la HBox
                for (int j = i; j < i + 3 && j < l.size(); j++) {
                    // Créer une VBox pour chaque defi
                    VBox ez = new VBox();
                    ez.setSpacing(10);
                    ez.setStyle("-fx-border-color: #da5f46; -fx-border-width: 2px; -fx-padding: 10px; -fx-alignment: center; -fx-background-color: #f0f0f0;"); // Ajouter une couleur de fond gris clair à la VBox
                    ez.setMinWidth(345); // Définir une largeur minimale pour toutes les VBox
                    ez.setMaxWidth(345); // Définir une largeur maximale pour toutes les VBox

                    Defi defi = l.get(j);
                    // Ajouter les éléments de l'defi à la VBox

                    Label nomLabel = new Label(defi.getNom());
                    Label titre = new Label("Exercies                                       S / R");
                    nomLabel.setStyle("-fx-alignment: center; -fx-font-size: 26; -fx-font-weight: bold;");
                    titre.setStyle("-fx-alignment: center; -fx-font-size: 18; -fx-font-weight: bold;");
                    Separator separator = new Separator();
                    separator.setPrefHeight(2);

                    Separator separator1 = new Separator();
                    separator1.setPrefHeight(2);

                    Separator separator2 = new Separator();
                    separator2.setPrefHeight(2);

                    ez.getChildren().addAll(nomLabel, separator, titre, separator1);

                    // Créer une grille pour afficher les valeurs sous chaque itération
                    GridPane grid = new GridPane();
                    grid.setHgap(10); // Espacement horizontal entre les éléments
                    grid.setVgap(5); // Espacement vertical entre les éléments

                    ArrayList<Exercice> lpl = defi.getLex();
                    Iterator<Exercice> iterator = lpl.iterator();
                    int row = 0;

                    // Parcourir les exercices et les afficher dans la grille
                    while (iterator.hasNext()) {
                        Exercice exercice = iterator.next();
                        Label nomExerciceLabel = new Label(exercice.getNom());
                        Label valeurLabel = new Label(Integer.parseInt(defi.getNd()) * 3 + " / " + Integer.parseInt(exercice.getNd()) * 5);

                        // Appliquer le style aux étiquettes
                        nomExerciceLabel.setStyle("-fx-text-fill: black;");
                        valeurLabel.setStyle("-fx-text-fill: black;");
                        nomExerciceLabel.setOnMouseClicked(event -> detail(exercice));

                        // Ajouter un espacement horizontal entre les étiquettes
                        GridPane.setMargin(valeurLabel, new Insets(0, 0, 0, 200)); // Espacement à gauche de la valeurLabel

                        // Ajouter les étiquettes à la grille
                        grid.add(nomExerciceLabel, 0, row); // Ajouter le nom de l'exercice à la colonne 0, ligne courante
                        grid.add(valeurLabel, 1, row); // Ajouter la valeur à la colonne 1, ligne courante
                        row++; // Passer à la ligne suivante pour le prochain exercice
                    }
                    // Ajouter la grille à la VBox
                    ez.getChildren().add(grid);

                    Label desLabel = new Label(defi.getDes());
                    desLabel.setStyle("-fx-text-fill: grey;"); // Définir la couleur grise pour le texte de mcLabel

                    ImageView ndImage;

                    if (defi.getNd().equals("1")) {
                        ndImage = new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/et1.png")));
                    } else if (defi.getNd().equals("2")) {
                        ndImage = new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/et2.png")));
                    } else {
                        ndImage = new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/et3.png")));
                    }
                    ndImage.setFitWidth(200);
                    ndImage.setFitHeight(33);

                    // Ajouter les éléments à la VBox
                    ez.getChildren().addAll(separator2, desLabel, ndImage);
                    hbox.getChildren().add(ez);
                }

                // Ajouter la HBox contenant les quatre defis à la VBox principale avec une marge
                VBox.setMargin(hbox, new Insets(10)); // Ajouter une marge autour de chaque HBox
                vbox.getChildren().add(hbox);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    private void detail(Exercice ex) {
        try {
            // Charger le fichier FXML de la nouvelle scène
            // Charger le fichier FXML ExerciceDetails.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Exercice/ExerciceDetails.fxml"));
            Parent root = fxmlLoader.load();

            // Obtenir le contrôleur associé au fichier FXML chargé
            ExerciceDetails exerciceDetails = fxmlLoader.getController();

            // Remplir les champs du contrôleur avec les données de l'exercice sélectionné

            exerciceDetails.setNomM(ex.getNom());
            exerciceDetails.setDesM(ex.getDes());
            exerciceDetails.setMcM(ex.getMc());
            exerciceDetails.setNdM(ex.getNd());
            exerciceDetails.setIiimage(ex.getImg());
            vbox.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
       
    }




    @FXML
    protected void AfficherEXSearch() {
        DefiService es = new DefiService();
        try {
            List<Defi> l = es.afficherListSearch(search.getText());

            // Effacer les données existantes de la VBox
            vbox.getChildren().clear();

            // Parcourir la liste des defis
            for (int i = 0; i < l.size(); i += 3) {
                // Créer une HBox pour chaque groupe de quatre defis
                HBox hbox = new HBox();
                hbox.setSpacing(20); // Ajouter plus d'espace entre les éléments de la HBox

                // Ajouter les quatre éléments de l'defi à la HBox
                for (int j = i; j < i + 3 && j < l.size(); j++) {
                    // Créer une VBox pour chaque defi
                    VBox ez = new VBox();
                    ez.setSpacing(10);
                    ez.setStyle("-fx-border-color: #da5f46; -fx-border-width: 2px; -fx-padding: 10px; -fx-alignment: center; -fx-background-color: #f0f0f0;"); // Ajouter une couleur de fond gris clair à la VBox
                    ez.setMinWidth(345); // Définir une largeur minimale pour toutes les VBox
                    ez.setMaxWidth(345); // Définir une largeur maximale pour toutes les VBox

                    Defi defi = l.get(j);
                    // Ajouter les éléments de l'defi à la VBox

                    Label nomLabel = new Label(defi.getNom());
                    Label titre = new Label("Exercies                                       S / R");
                    nomLabel.setStyle("-fx-alignment: center; -fx-font-size: 26; -fx-font-weight: bold;");
                    titre.setStyle("-fx-alignment: center; -fx-font-size: 18; -fx-font-weight: bold;");
                    Separator separator = new Separator();
                    separator.setPrefHeight(2);

                    Separator separator1 = new Separator();
                    separator1.setPrefHeight(2);

                    Separator separator2 = new Separator();
                    separator2.setPrefHeight(2);

                    ez.getChildren().addAll(nomLabel, separator, titre, separator1);

                    // Créer une grille pour afficher les valeurs sous chaque itération
                    GridPane grid = new GridPane();
                    grid.setHgap(10); // Espacement horizontal entre les éléments
                    grid.setVgap(5); // Espacement vertical entre les éléments

                    ArrayList<Exercice> lpl = defi.getLex();
                    Iterator<Exercice> iterator = lpl.iterator();
                    int row = 0;

                    // Parcourir les exercices et les afficher dans la grille
                    while (iterator.hasNext()) {
                        Exercice exercice = iterator.next();
                        Label nomExerciceLabel = new Label(exercice.getNom());
                        Label valeurLabel = new Label(Integer.parseInt(defi.getNd()) * 3 + " / " + Integer.parseInt(exercice.getNd()) * 5);

                        // Appliquer le style aux étiquettes
                        nomExerciceLabel.setStyle("-fx-text-fill: black;");
                        valeurLabel.setStyle("-fx-text-fill: black;");
                        nomExerciceLabel.setOnMouseClicked(event -> detail(exercice));

                        // Ajouter un espacement horizontal entre les étiquettes
                        GridPane.setMargin(valeurLabel, new Insets(0, 0, 0, 200)); // Espacement à gauche de la valeurLabel

                        // Ajouter les étiquettes à la grille
                        grid.add(nomExerciceLabel, 0, row); // Ajouter le nom de l'exercice à la colonne 0, ligne courante
                        grid.add(valeurLabel, 1, row); // Ajouter la valeur à la colonne 1, ligne courante
                        row++; // Passer à la ligne suivante pour le prochain exercice
                    }
                    // Ajouter la grille à la VBox
                    ez.getChildren().add(grid);

                    Label desLabel = new Label(defi.getDes());
                    desLabel.setStyle("-fx-text-fill: grey;"); // Définir la couleur grise pour le texte de mcLabel

                    ImageView ndImage;

                    if (defi.getNd().equals("1")) {
                        ndImage = new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/et1.png")));
                    } else if (defi.getNd().equals("2")) {
                        ndImage = new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/et2.png")));
                    } else {
                        ndImage = new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/et3.png")));
                    }
                    ndImage.setFitWidth(200);
                    ndImage.setFitHeight(33);

                    // Ajouter les éléments à la VBox
                    ez.getChildren().addAll(separator2, desLabel, ndImage);
                    hbox.getChildren().add(ez);
                }

                // Ajouter la HBox contenant les quatre defis à la VBox principale avec une marge
                VBox.setMargin(hbox, new Insets(10)); // Ajouter une marge autour de chaque HBox
                vbox.getChildren().add(hbox);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }







    public void Defis( ) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/DefiV.fxml"));
            primaryStage=(Stage)vbox.getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }

    public void Exercices( ) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/ExerciceV.fxml"));
            primaryStage=(Stage)vbox.getScene().getWindow();
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

