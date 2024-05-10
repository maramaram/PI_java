package Controller;

import Entities.Exercice;
import Service.ExerciceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Entities.SessionManager;
import Entities.User;
import Service.UserService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ExerciceV {


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

            ObservableList<String> valeurs = FXCollections.observableArrayList("Nom", "Muscle ciblé", "Niveau de difficulté");
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
    protected void AfficherEX() {
        ExerciceService es = new ExerciceService();
        try {
            List<Exercice> l = es.afficherListTri(wez, wezz);

            // Créer une pagination
            Pagination pagination = new Pagination((l.size() + 8) / 9, 0); // (total d'éléments + éléments par page - 1) / éléments par page
            pagination.setPageFactory(pageIndex -> createPage(l, pageIndex));

            // Ajouter la pagination à la VBox
            vbox.getChildren().clear();
            vbox.getChildren().add(pagination);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private VBox createPage(List<Exercice> l, int pageIndex) {
        VBox page = new VBox();
        page.setSpacing(10);

        int startIndex = pageIndex * 9; // Index de début pour cette page
        int endIndex = Math.min(startIndex + 9, l.size()); // Index de fin pour cette page

        // Parcourir la liste des exercices pour cette page
        for (int i = startIndex; i < endIndex; i += 3) {
            // Créer une HBox pour chaque groupe de quatre exercices
            HBox hbox = new HBox();
            hbox.setSpacing(20); // Ajouter plus d'espace entre les éléments de la HBox

            // Ajouter les quatre éléments de l'exercice à la HBox
            for (int j = i; j < i + 3 && j < endIndex; j++) {
                // Créer une VBox pour chaque exercice
                VBox ez = new VBox();
                ez.setSpacing(10);
                ez.setStyle("-fx-border-color: #da5f46; -fx-border-width: 2px; -fx-padding: 10px; -fx-alignment: center; -fx-background-color: #f0f0f0;"); // Ajouter une couleur de fond gris clair à la VBox
                ez.setMinWidth(340); // Définir une largeur minimale pour toutes les VBox
                ez.setMaxWidth(340); // Définir une largeur maximale pour toutes les VBox

                Exercice exercice = l.get(j);
                // Ajouter les éléments de l'exercice à la VBox
                ImageView eImage = exercice.getImage();
                eImage.setFitWidth(175);
                eImage.setFitHeight(125);
                eImage.setStyle("-fx-border-color: #da5f46; -fx-border-width: 5px; -fx-border-radius: 10px;"); // Ajouter un contour noir autour de l'image avec des coins arrondis
                eImage.setOnMouseClicked(event -> detail(exercice)); // Ajouter un gestionnaire d'événements au clic de la souris sur l'image
                Label nomLabel = new Label(exercice.getNom());
                nomLabel.setStyle("-fx-alignment: center; -fx-font-size: 26; -fx-font-weight: bold;");
                nomLabel.setOnMouseClicked(event -> detail(exercice));
                Label mcLabel = new Label(exercice.getMc());
                mcLabel.setStyle("-fx-text-fill: grey;"); // Définir la couleur grise pour le texte de mcLabel
                ImageView ndImage;
                if (exercice.getNd().equals("1")) {
                    ndImage = new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/dif1.png")));
                } else if (exercice.getNd().equals("2")) {
                    ndImage = new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/dif2.png")));
                } else {
                    ndImage = new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/dif3.png")));
                }
                ndImage.setFitWidth(100);
                ndImage.setFitHeight(75);

                // Ajouter les éléments à la VBox
                ez.getChildren().addAll(eImage, nomLabel, mcLabel, ndImage);
                hbox.getChildren().add(ez);
            }

            // Ajouter la HBox contenant les quatre exercices à la VBox principale avec une marge
            VBox.setMargin(hbox, new Insets(10)); // Ajouter une marge autour de chaque HBox
            page.getChildren().add(hbox);
        }

        return page;
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
            exerciceDetails.setIiimage(ex.getGif());
            vbox.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }



    @FXML
private void Tri(){
if (choice.getValue().equals("Nom"))
    wez=1;
else if (choice.getValue().equals("Muscle ciblé"))
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



    @FXML
    protected void AfficherEXSearch() {
        ExerciceService es = new ExerciceService();
        try {
            List<Exercice> l = es.afficherListSearchTri(search.getText(),wez,wezz);
            // Créer une pagination
            Pagination pagination = new Pagination((l.size() + 8) / 9, 0); // (total d'éléments + éléments par page - 1) / éléments par page
            pagination.setPageFactory(pageIndex -> createPage(l, pageIndex));

            // Ajouter la pagination à la VBox
            vbox.getChildren().clear();
            vbox.getChildren().add(pagination);

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

    public void Exercices() {
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

