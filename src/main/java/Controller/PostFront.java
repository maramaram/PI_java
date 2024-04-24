package Controller;

import Entities.Exercice;
import Entities.Post;
import Entities.Post;
import Service.ExerciceService;
import Service.PostService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;


public class PostFront {


    private Stage primaryStage;

    @FXML
    private Label titleE;
    @FXML
    private Label contenuE;
    @FXML
    private Label dateE;
    @FXML
    private Label imageE;
    @FXML
    private Label viewsE;

    @FXML
    private TextField search;








    @FXML
    private TableView table;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn title;
    @FXML
    private TableColumn contenu;
    @FXML
    private TableColumn date;
    @FXML
    private TableColumn image;
    @FXML
    private TableColumn views;










    @FXML
    public void initialize() {

        AfficherEX(); // Appeler la méthode pour afficher les données
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifier si c'est un double-clic
                // Convertir l'objet sélectionné en Exercice en utilisant le casting
                Post post = (Post) table.getSelectionModel().getSelectedItem();
                if (post != null) {
                    try {
                        // Charger le fichier FXML ExerciceUpdate.fxml
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Post/PostUpdate.fxml"));
                        Parent root = fxmlLoader.load();

                        // Obtenir le contrôleur associé au fichier FXML chargé
                        PostUpdate postUpdate = fxmlLoader.getController();

                        // Remplir les champs du contrôleur avec les données de l'post sélectionné
                        postUpdate.setIdM(String.valueOf(post.getId()));
                        postUpdate.setTitleM(post.getTitle());
                        postUpdate.setContenuM(post.getContenu());


                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        postUpdate.setDateM(dateFormat.format(post.getDate()));

                        postUpdate.setViewsM(String.valueOf(post.getViews()));
                        postUpdate.setImageM(post.getImage());


                        // Remplacer la racine de la scène par le nouveau contenu chargé à partir du fichier FXML
                        table.getScene().setRoot(root);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }





    @FXML
    protected void supprimerEX() {

        TableView.TableViewSelectionModel<Post> selectionModel = table.getSelectionModel();

// Vérifier s'il y a une ligne sélectionnée
        if (!selectionModel.isEmpty()) {
            PostService es = new PostService();
            // Récupérer l'objet Post correspoimageAnt à la ligne sélectionnée
            Post selectedPost = selectionModel.getSelectedItem();
            try {
            es.delete(selectedPost);
            AfficherEX();
            }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }




    protected void AfficherEX() {
        PostService es = new PostService();
        try {
            List<Post> l = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Post post : l) {
                table.getItems().add(post);
            }

            // Associer les propriétés des objets Post aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            views.setCellValueFactory(new PropertyValueFactory<>("views"));
            image.setCellValueFactory(new PropertyValueFactory<>("image1"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }





    public void AJ(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Post/PostAdd.fxml"));
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



/*    public void Comments(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Comment/Comment-front.fxml"));
            primaryStage=(Stage)((Node)event.getSource()).getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }*/


    @FXML
    public void handleButtonClick(ActionEvent event) {
        // Obtenir le modèle de sélection de la table
        TableView.TableViewSelectionModel<Post> selectionModel = table.getSelectionModel();
        // Vérifier si un post est sélectionné
        if (!selectionModel.isEmpty()) {
            // Récupérer le post sélectionné
            Post post = (Post) table.getSelectionModel().getSelectedItem();

            // Assurez-vous que le post sélectionné n'est pas null et a un ID
            if (post != null) {
                // Récupérer l'ID du post sélectionné
                int selectedPostId = post.getId(); // Assurez-vous que Post a une méthode getId()

                // Appeler la méthode pour charger la nouvelle scène avec l'ID du post sélectionné
                navigateToCommentsScene(event, selectedPostId);
            }
        } else {
            // Afficher un message ou gérer le cas où aucun post n'est sélectionné
            System.out.println("Aucun post sélectionné.");
        }
    }

    private void navigateToCommentsScene(ActionEvent event, int selectedPostId) {
            try {
                // Charger le fichier FXML de la nouvelle scène
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Comment/Comment-front.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur de la nouvelle scène
                CommentFront controller = loader.getController();

                // Passer l'ID du post sélectionné au contrôleur
                controller.setPost_id(selectedPostId);

                // Créer une nouvelle scène avec le root chargé
                Scene newScene = new Scene(root);

                // Obtenir la scène actuelle (Stage)
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Définir la nouvelle scène sur le stage principal
                primaryStage.setScene(newScene);

                // Afficher la nouvelle scène
                primaryStage.show();
            } catch (IOException e) {
                // Gérer l'erreur de chargement de la nouvelle scène
                System.err.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
            }
    }







    @FXML
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

    @FXML
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
    public void Blogs(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Post/Post-front.fxml"));
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
    protected void AfficherEXSearch() {
        PostService es = new PostService();
        try {
            List<Post> l = es.afficherListSearch(search.getText());

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Post post : l) {
                table.getItems().add(post);
            }

            // Associer les propriétés des objets Post aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            views.setCellValueFactory(new PropertyValueFactory<>("views"));
            image.setCellValueFactory(new PropertyValueFactory<>("image1"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}