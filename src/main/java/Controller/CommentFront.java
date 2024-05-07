package Controller;

import Entities.Comment;
import Entities.Post;
import Service.CommentService;
import Service.PostService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CommentFront {

    private Stage primaryStage;

    @FXML
    private TableView<Comment> table; // Type générique précisé
    @FXML
    private TableColumn<Comment, Integer> id; // Type générique précisé
    @FXML
    private TableColumn<Comment, Integer> nblikes; // Type générique précisé
    @FXML
    private TableColumn<Comment, String> contenu; // Type générique précisé

    private int post_Id; // Variable pour stocker l'ID du post

    public void setPost_id(int selectedPostId) {
        // Stocker l'ID du post dans la variable de la classe
        this.post_Id = selectedPostId;

        // Appeler la méthode pour afficher les commentaires associés à cet ID de post
        AfficherCommentaires(post_Id);
    }

    @FXML
    public void initialize() {

            AfficherCommentaires(post_Id);

    }

    protected void AfficherCommentaires(int postId) {
        // Créez une instance de CommentService
        CommentService commentService = new CommentService();

        try {
            // Récupérez la liste des commentaires associés à un post spécifique
            List<Comment> comments = commentService.afficherListByPostId(postId);

            // Effacez les données existantes de la TableView
            table.getItems().clear();

            // Ajoutez les commentaires récupérés à la TableView
            table.getItems().addAll(comments);

            // Configurez les colonnes pour afficher les propriétés des objets Comment
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nblikes.setCellValueFactory(new PropertyValueFactory<>("nblikes")); // Vérifiez le nom de la propriété ici
            contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));

        } catch (SQLException e) {
            // Affichez le message d'erreur dans la console
            System.out.println("Erreur lors de l'affichage des commentaires : " + e.getMessage());
        }
    }


    @FXML
    protected void supprimerEX() {

        TableView.TableViewSelectionModel<Comment> selectionModel = table.getSelectionModel();

// Vérifier s'il y a une ligne sélectionnée
        if (!selectionModel.isEmpty()) {
            CommentService es = new CommentService();
            // Récupérer l'objet Post correspoimageAnt à la ligne sélectionnée
            Comment selectedComment = selectionModel.getSelectedItem();
            try {
                es.delete(selectedComment);
                AfficherCommentaires(post_Id);
            }catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }

    // Les méthodes qui changent de scène
    public void Exercices(ActionEvent event) {
        changerScene(event, "/Exercice/Exercice-front.fxml");
    }

    public void Defis(ActionEvent event) {
        changerScene(event, "/Defi/Defi-front.fxml");
    }

    public void Blogs(ActionEvent event) {
        changerScene(event, "/Post/Post-front.fxml");
    }

    // Méthode pour changer de scène
    private void changerScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }


    @FXML
    protected void AfficherEXSearch() {

    }


}
