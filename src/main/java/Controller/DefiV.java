package Controller;

import Entities.Defi;
import Entities.Exercice;
import Service.DefiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Entities.SessionManager;
import Entities.User;
import Service.UserService;
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
    private ChoiceBox<String> choice;

    @FXML
    private ChoiceBox<String> choice1;

    private int wez=0,wezz=1;

    @FXML
    public void initialize() {

            ObservableList<String> valeurs = FXCollections.observableArrayList("Nom", "Nombre de jours", "Niveau de difficulté");
            choice.setItems(valeurs);

            ObservableList<String> valeurss = FXCollections.observableArrayList("ASC", "DESC");
            choice1.setItems(valeurss);
            choice1.setValue("ASC");

            choice.setOnAction(event -> {
                // Appelez votre fonction de tri ici
                Tri();
            });

            choice1.setOnAction(event -> {
                // Appelez votre fonction de tri ici
                Tri();
            });
            AfficherEX(); // Appeler la méthode pour afficher les données

        }


    @FXML
    private void Tri(){
        if (choice.getValue().equals("Nom"))
            wez=1;
        else if (choice.getValue().equals("Nombre de jours"))
            wez=2;
        else if (choice.getValue().equals("Niveau de difficulté"))
            wez=3;
        else
            wez=0;


        if (choice1.getValue().equals("ASC"))
            wezz=1;
        else
            wezz=2;

        AfficherEXSearch();
    }



    private VBox createPage(List<Defi> l, int pageIndex) {
        VBox page = new VBox();
        page.setSpacing(10);

        int startIndex = pageIndex * 9; // Index of the first defi on this page
        int endIndex = Math.min(startIndex + 9, l.size()); // Index of the last defi on this page

        // Loop through the defis on this page
        for (int i = startIndex; i < endIndex; i += 3) {
            // Create an HBox for each group of three defis
            HBox hbox = new HBox();
            hbox.setSpacing(20); // Add more space between the elements of the HBox

            // Add the three defi elements to the HBox
            for (int j = i; j < i + 3 && j < endIndex; j++) {
                // Create a VBox for each defi
                VBox ez = new VBox();
                ez.setSpacing(10);
                ez.setStyle("-fx-border-color: #da5f46; -fx-border-width: 2px; -fx-padding: 10px; -fx-alignment: center; -fx-background-color: #f0f0f0;"); // Add a light gray background color to the VBox
                ez.setMinWidth(343); // Set a minimum width for all VBoxes
                ez.setMaxWidth(343); // Set a maximum width for all VBoxes

                Defi defi = l.get(j);
                // Add the elements of the defi to the VBox

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

                // Create a grid to display the values under each iteration
                GridPane grid = new GridPane();
                grid.setHgap(10); // Horizontal spacing between elements
                grid.setVgap(5); // Vertical spacing between elements

                ArrayList<Exercice> lpl = defi.getLex();
                Iterator<Exercice> iterator = lpl.iterator();
                int row = 0;

                // Loop through the exercises and display them in the grid
                while (iterator.hasNext()) {
                    Exercice exercice = iterator.next();
                    Label nomExerciceLabel = new Label(exercice.getNom());
                    Label valeurLabel = new Label(Integer.parseInt(defi.getNd()) * 3 + " / " + Integer.parseInt(exercice.getNd()) * 5);

                    // Apply style to labels
                    nomExerciceLabel.setStyle("-fx-text-fill: black;");
                    valeurLabel.setStyle("-fx-text-fill: black;");
                    nomExerciceLabel.setOnMouseClicked(event -> detail(exercice));

                    // Add horizontal spacing between labels
                    GridPane.setMargin(valeurLabel, new Insets(0, 0, 0, 19)); // Left spacing of valeurLabel

                    // Add the labels to the grid
                    grid.add(nomExerciceLabel, 0, row); // Add the exercise name to column 0, current row
                    grid.add(valeurLabel, 1, row); // Add the value to column 1, current row
                    row++; // Move to the next row for the next exercise
                }
                // Add the grid to the VBox
                ez.getChildren().add(grid);

                Label desLabel = new Label(defi.getDes());
                Label nbjLabel = new Label(String.valueOf(defi.getNbj()));
                desLabel.setStyle("-fx-text-fill: grey;"); // Set gray color for the text of mcLabel

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

                // Add the elements to the VBox
                ez.getChildren().addAll(separator2,desLabel,nbjLabel, ndImage);
                hbox.getChildren().add(ez);
            }

            // Add the HBox containing the three defis to the VBox with a margin
            VBox.setMargin(hbox, new Insets(10)); // Add a margin around each HBox
            page.getChildren().add(hbox);
        }

        return page;
    }

    @FXML
    protected void AfficherEX() {
        DefiService es = new DefiService();
        try {
            List<Defi> l = es.afficherListTri(wez,wezz);

            // Create a pagination object
            Pagination pagination = new Pagination((l.size() + 8) / 9, 0); // (total number of items + items per page - 1) / items per page
            pagination.setPageFactory(pageIndex -> createPage(l, pageIndex));

            // Add the pagination object to the VBox
            vbox.getChildren().clear();
            vbox.getChildren().add(pagination);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @FXML
    protected void AfficherEXSearch() {
        DefiService es = new DefiService();
        try {
            List<Defi> l = es.afficherListSearchTri(search.getText(),wez,wezz);

            Pagination pagination = new Pagination((l.size() + 8) / 9, 0); // (total number of items + items per page - 1) / items per page
            pagination.setPageFactory(pageIndex -> createPage(l, pageIndex));
            // Add the pagination object to the VBox
            vbox.getChildren().clear();
            vbox.getChildren().add(pagination);

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

