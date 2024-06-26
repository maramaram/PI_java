package Controller;

import Entities.Defi;
import Entities.Exercice;
import Service.DefiService;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import Entities.SessionManager;
import Entities.User;
import Service.UserService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DefiAdd {

    @FXML
    private Label nomE;
    @FXML
    private Label desE;
    @FXML
    private Label ndE;
    @FXML
    private Label nbjE;
    @FXML
    private Label exE;

    @FXML
    private TableView table;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn nom;
    @FXML
    private TableColumn etat;

    @FXML
    private TextField nomA;
    @FXML
    private TextArea desA;
    @FXML
    private ChoiceBox<String> ndA;
    @FXML
    private TextField nbjA;



    private Stage primaryStage;


    private List<Exercice> ex;

    public List<Exercice> getEx() {
        return ex;
    }

    public void setEx(List<Exercice> ex) {
        this.ex = ex;

    }




    public void supprimerExerciceParId(int id, List<Exercice> lex) {
        // Parcourir la liste d'exercices
        Iterator<Exercice> iterator = lex.iterator();
        while (iterator.hasNext()) {
            Exercice exercice = iterator.next();
            // Vérifier si l'identifiant de l'exercice correspond à celui fourni
            if (exercice.getId() == id) {
                // Si c'est le cas, supprimer l'exercice de la liste
                iterator.remove();
                // Arrêter la boucle car nous avons trouvé et supprimé l'exercice
                break;
            }
        }
    }

    @FXML
    public void initialize() {

            // Appeler la méthode pour afficher les données
            AfficherEX();
            ObservableList<String> valeurs = FXCollections.observableArrayList("Facile", "Moyen", "Difficile");
            ndA.setItems(valeurs);
            // Ajouter un écouteur sur la table pour détecter les double-clics
            table.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Vérifier si c'est un double-clic
                    // Convertir l'objet sélectionné en Exercice en utilisant le casting
                    Exercice exercice = (Exercice) table.getSelectionModel().getSelectedItem();
                    if (exercice != null) {
                        if (exercice.isTest()) {
                            supprimerExerciceParId(exercice.getId(), ex);
                        } else {
                            if (ex != null)
                                ex.add(exercice);
                            else {
                                ex = new ArrayList<>();
                                ex.add(exercice);
                            }
                        }
                    }
                    AfficherEX();
                }
            });
        }


    // Méthode appelée lorsque le bouton est cliqué
    public void EX() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/Defi-front.fxml"));
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
    protected void AjouterEX() {
        DefiService es = new DefiService();
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
        


        if (ndA.getValue() == null || ndA.getValue().isEmpty() ||( !ndA.getValue().equals("Facile") &&
                !ndA.getValue().equals("Moyen") &&
                !ndA.getValue().equals("Difficile") )) {
            test=false;
            ndE.setText("Veuillez choisir parmi facile , moyen ou difficile");
        }else {ndE.setText("");}


        if (nbjA.getText().isEmpty() || !(nbjA.getText().matches("[1-9]|[12][0-9]|3[0-1]"))) {
            test = false;
            nbjE.setText("Le nombre doit être compris entre 1 et 31");
        } else {
            nbjE.setText("");
        }


        


           if (test){
            String a;
            if(ndA.getValue().equals("Facile")) a="1";
            else if (ndA.getValue().equals("Moyen")) a="2";
             else a="3";
               Defi de;
               if(ex!=null)
            {
                de = new Defi(nomA.getText(), desA.getText(),
                        a, Integer.parseInt(nbjA.getText()), new ArrayList<>(ex));
            }else {
                   de = new Defi(nomA.getText(), desA.getText(),
                           a, Integer.parseInt(nbjA.getText()), new ArrayList<>());
                }

            try{
            es.add(de);
                EX();
                ImageView commentaireIcon = new ImageView(new Image(getClass().getResourceAsStream("/Front/images/exo/ez.png")));
                commentaireIcon.setFitHeight(50);
                commentaireIcon.setFitWidth(50);
                Notifications.create()
                        .title("Succès")
                        .text("Le defi a été a jouté avec succées !!")
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
               alert.setContentText("Echec de l'ajout du defi !!");
               alert.showAndWait();
           }
    }

    @FXML
    protected void AfficherEX() {
        ExerciceService es = new ExerciceService();
        try {
            List<Exercice> l = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();
            // Ajouter les données de la liste à la TableView
            //System.out.println(ex);
            for (Exercice exercice : l) {
                if(ex !=null) {
                    if (ex.contains(exercice)) {
                        exercice.setEtat(new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/green.png"))));
                        exercice.setTest(true);
                    } else {
                        exercice.setEtat(new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/red.png"))));
                        exercice.setTest(false);
                    }
                }
                else
                {
                    exercice.setEtat(new ImageView(new Image(this.getClass().getResourceAsStream("/Front/images/exo/red.png"))));
                    exercice.setTest(false);
                }
                table.getItems().add(exercice);

            }

            // Associer les propriétés des objets Exercice aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            etat.setCellValueFactory(new PropertyValueFactory<>("etat"));


        } catch (SQLException e) {
            System.out.println(e.getMessage());
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