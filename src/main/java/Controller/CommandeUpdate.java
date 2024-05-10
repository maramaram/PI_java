package Controller;

import Entities.*;
import Service.CommandeService;
import Service.LivreurService;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class CommandeUpdate {

    @FXML
    private Label LivreurIdE;
    @FXML
    private Label UserIdE;
    @FXML
    private Label StatutE;
    @FXML
    private Label PrixTotalE;


    @FXML
    private TextField idA;
    @FXML
    private TextField LivreurIdA;
    @FXML
    private ChoiceBox<UserItem> userChoiceBox;
    @FXML
    private ChoiceBox<String> StatutA;
    @FXML
    private TextField PrixTotalA;

    @FXML
    private ChoiceBox<LivreurItem> livreurChoiceBox;
    private Stage primaryStage;



    public void setIdA(String idA) {
        this.idA.setText(idA);
    }

    public void setLivreurChoiceBoxValue(int livreurId) {
        for (LivreurItem item : livreurChoiceBox.getItems()) {
            if (item.getId() == livreurId) {
                livreurChoiceBox.setValue(item);
                break;
            }
        }
    }
    public void setUserChoiceBoxValue(int userId) {
        for (UserItem item : userChoiceBox.getItems()) {
            if (item.getId() == userId) {
                userChoiceBox.setValue(item);
                break;
            }
        }
    }
    public void setStatutA(String StatutA) {
        this.StatutA.setValue(StatutA);
    }

    public void setPrixTotalA(String PrixTotalA) {
        this.PrixTotalA.setText(PrixTotalA);
    }

    public void initialize() {
        StatutA.getItems().addAll("en route", "arrivée", "en attente");

        LivreurService livreurService = new LivreurService();

        // Populate the livreurChoiceBox with livreur names and IDs
        try {
            List<Livreur> livreurs = livreurService.afficherList();

            for (Livreur livreur : livreurs) {
                String name = livreur.getNom() + " " + livreur.getPrenom();
                LivreurItem livreurItem = new LivreurItem(livreur.getId(), name);
                livreurChoiceBox.getItems().add(livreurItem);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching livreurs: " + e.getMessage());
        }
        UserService userService = new UserService(); // Assuming this is your UserService

        // Populate the livreurChoiceBox with user names and IDs
        List<User> users = userService.getAllUsers(); // Fetch all users from database

        for (User user : users) {
            String name = user.getNom() + " " + user.getPrenom();
            UserItem userItem = new UserItem(Integer.parseInt(user.getId()), name); // Create a UserItem with userId and name
            userChoiceBox.getItems().add(userItem); // Add the UserItem to the choiceBox
        }
    }
    // Méthode appelée lorsque le bouton est cliqué
    public void Lib() {
        try {
            // Charger le fichier FXML de la nouvelle scène
            Parent root = FXMLLoader.load(getClass().getResource("/Commande/CommandeBack.fxml"));
            primaryStage=(Stage)livreurChoiceBox.getScene().getWindow();
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
    protected void Mod() {
        CommandeService es = new CommandeService();
        boolean test=true;
        // Vérifier si tous les champs de texte sont remplis
        if (livreurChoiceBox.getValue() == null) {
            test = false;
            LivreurIdE.setText("vide");
        } else {
            LivreurIdE.setText("");
        }


        UserItem selectedUser = userChoiceBox.getValue();
        if (selectedUser == null) {
            test = false;
            UserIdE.setText("vide");
        } else {
            UserIdE.setText("");
        }

        if (StatutA.getValue() == null) {
            test = false;
            StatutE.setText("vide");
        } else {
            StatutE.setText("");
        }

        if (PrixTotalA.getText().isEmpty()) {
            test = false;
            PrixTotalE.setText("vide");
        } else {
            String PrixTotalText = PrixTotalA.getText();
            if (!PrixTotalText.matches("^[0-9]+$")) {
                test = false;
                PrixTotalE.setText("Le PrixTotal ne doit contenir que des nombres");
            } else {
                PrixTotalE.setText("");
            }
        }

        if (test){
            LivreurItem selectedLivreur = livreurChoiceBox.getValue();
            Commande ex = new Commande(Integer.parseInt(idA.getText()),selectedLivreur.getId(), selectedUser.getId()
                    , StatutA.getValue(),Integer.parseInt(PrixTotalA.getText()));
            try{
                es.modifier(ex);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("Le Commande a été a modifié avec succées !!");
                alert.showAndWait();
                Lib();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else {

            System.out.println("Veuillez remplir tous les champs.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Echec");
            alert.setContentText("Echec de la modification du Commande !!");
            alert.showAndWait();
        }
    }

}