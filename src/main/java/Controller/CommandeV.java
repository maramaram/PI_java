package Controller;

import Entities.Commande;
import Entities.SessionManager;
import Service.CommandeService;
import Entities.Livreur;
import Service.LivreurService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CommandeV {

    @FXML
    private VBox vbox;
    String userId = SessionManager.getInstance().getUserId();
    @FXML
    public void initialize() {
        afficherCommandes();
    }

    @FXML
    protected void afficherCommandes() {
        CommandeService commandeService = new CommandeService();
        try {
            List<Commande> commandes = commandeService.afficherListwithuserid(userId);

            // Effacer les données existantes de la VBox
            vbox.getChildren().clear();

            // Parcourir la liste des commandes
            for (int i = 0; i < commandes.size(); i += 3) {
                // Créer une HBox pour chaque groupe de trois commandes
                HBox hbox = new HBox();
                hbox.setSpacing(20); // Ajouter plus d'espace entre les éléments de la HBox

                // Ajouter les trois commandes à la HBox
                for (int j = i; j < i + 3 && j < commandes.size(); j++) {
                    // Créer une VBox pour chaque commande
                    VBox commandeBox = new VBox();
                    commandeBox.setSpacing(10);
                    commandeBox.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #e0e0e0; -fx-border-width: 2px; -fx-padding: 10px;"); // Ajouter une couleur de fond gris clair à la VBox et une bordure grise
                    commandeBox.setMinWidth(345); // Définir une largeur minimale pour toutes les VBox
                    commandeBox.setMaxWidth(345); // Définir une largeur maximale pour toutes les VBox

                    final Commande commande = commandes.get(j);

                    // Créer une GridPane pour afficher les valeurs sous chaque itération
                    GridPane grid = new GridPane();
                    grid.setHgap(10); // Espacement horizontal entre les éléments
                    grid.setVgap(5); // Espacement vertical entre les éléments

                    // Ajouter les éléments de la commande à la GridPane
                    Label idLabel = new Label("ID : ");
                    Label idValueLabel = new Label(commande.getId() + "");
                    Label livreurIdLabel = new Label("Livreur ID : ");
                    Label livreurIdValueLabel = new Label(commande.getLivreurId() + "");
                    Label userIdLabel = new Label("User ID : ");
                    Label userIdValueLabel = new Label(commande.getUserId() + "");
                    Label statutLabel = new Label("Statut : ");
                    Label statutValueLabel = new Label(commande.getStatut());
                    Label prixTotalLabel = new Label("Prix total : ");
                    Label prixTotalValueLabel = new Label(commande.getPrixTotal() + "");

                    idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    livreurIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    userIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    statutLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    prixTotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                    grid.add(idLabel, 0, 0);
                    grid.add(idValueLabel, 1, 0);
                    grid.add(livreurIdLabel, 0, 1);
                    grid.add(livreurIdValueLabel, 1, 1);
                    grid.add(userIdLabel, 0, 2);
                    grid.add(userIdValueLabel, 1, 2);
                    grid.add(statutLabel, 0, 3);
                    grid.add(statutValueLabel, 1, 3);
                    grid.add(prixTotalLabel, 0, 4);
                    grid.add(prixTotalValueLabel, 1, 4);

                    // Ajouter un écouteur d'événements à la VBox pour détecter les clics
                    commandeBox.setOnMouseClicked(event -> {
                        // Appeler la méthode detail() avec le Livreur sélectionné
                        detail(commande);
                    });

                    commandeBox.getChildren().add(grid);
                    hbox.getChildren().add(commandeBox);
                }

                // Ajouter la HBox contenant les trois commandes à la VBox principale avec une marge
                VBox.setMargin(hbox, new Insets(10)); // Ajouter une marge autour de chaque HBox
                vbox.getChildren().add(hbox);
            }

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void detail(Commande commande) {
        try {
            // Retrieve the Livreur object associated with the Commande
            LivreurService livreurService = new LivreurService();
            Livreur livreur = livreurService.getLivreurById(commande.getLivreurId());

            // Load the LivreurDetail.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Livreur/LivreurDetail.fxml"));

            Parent root = fxmlLoader.load();

            // Get the LivreurDetail controller
            LivreurDetail livreurDetailController = fxmlLoader.getController();

            // Fill the fields of the controller with the data of the selected Livreur
            livreurDetailController.setNomM(livreur.getNom());
            livreurDetailController.setprenomM(livreur.getPrenom());
            livreurDetailController.setdisponibiliteM(livreur.getDisponibilite());
            livreurDetailController.setnoteM(String.valueOf(livreur.getNote()));
            livreurDetailController.setImage(livreur.getImage());

            // Set the root of the scene to the LivreurDetail.fxml file
            vbox.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des données du livreur : " + e.getMessage());
        }
    }



}

