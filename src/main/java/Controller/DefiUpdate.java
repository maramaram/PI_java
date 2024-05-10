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


public class DefiUpdate {




    @FXML
    private Label nomEE;
    @FXML
    private Label desEE;
    @FXML
    private Label ndEE;
    @FXML
    private Label nbjEE;
    @FXML
    private Label exEE;

    @FXML
    private TableView table;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn nom;
    @FXML
    private TableColumn etat;



    @FXML
    private TextField idM;
    @FXML
    private TextField nomM;
    @FXML
    private TextArea desM;
    @FXML
    private ChoiceBox<String> ndM;
    @FXML
    private TextField nbjM;

    private List<Exercice> ex;

    public List<Exercice> getEx() {
        return ex;
    }

    public void setEx(List<Exercice> ex) {
        this.ex = ex;
        AfficherEX();
    }

    public void setIdM(String idM) {
        this.idM.setText(idM);
    }

    public void setNomM(String nomM) {
        this.nomM.setText(nomM);
    }

    public void setDesM(String desM) {
        this.desM.setText(desM);
    }
    
    public void setNdM(String ndM) {
        if (ndM.equals("1"))
            this.ndM.setValue("Facile");
        else if (ndM.equals("2"))
            this.ndM.setValue("Moyen");
        else this.ndM.setValue("Difficile");
    }

    public void setNbjM(String nbjM) {
        this.nbjM.setText(nbjM);
    }
    

    private Stage primaryStage;

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
            ObservableList<String> valeurs = FXCollections.observableArrayList("Facile", "Moyen", "Difficile");
            ndM.setItems(valeurs);
            // Ajouter un écouteur sur la table pour détecter les double-clics
            table.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Vérifier si c'est un double-clic
                    // Convertir l'objet sélectionné en Exercice en utilisant le casting
                    Exercice exercice = (Exercice) table.getSelectionModel().getSelectedItem();
                    if (exercice != null) {
                        if (exercice.isTest()) {
                            supprimerExerciceParId(exercice.getId(), ex);
                        } else {
                            ex.add(exercice);
                        }
                    }
                    AfficherEX();
                }
            });
        }



    public void EX() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/Defi-front.fxml"));
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
        DefiService es = new DefiService();
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


        


        if (ndM.getValue() == null || ndM.getValue().isEmpty() ||(!ndM.getValue().equals("Facile") &&
                !ndM.getValue().equals("Moyen") &&
                !ndM.getValue().equals("Difficile") )) {
            test=false;
            ndEE.setText("Veuillez choisir parmi facile , moyen ou difficile");
        }else {ndEE.setText("");}


        if (nbjM.getText().isEmpty() || !(nbjM.getText().matches("[1-9]|[12][0-9]|3[0-1]"))) {
            test = false;
            nbjEE.setText("Le nombre doit être compris entre 1 et 31");
        } else {
            nbjEE.setText("");
        }



        if (test){
            String a;
            if(ndM.getValue().equals("Facile")) a="1";
            else if (ndM.getValue().equals("Moyen")) a="2";
            else a="3";
            Defi de = new Defi(Integer.parseInt(idM.getText()),nomM.getText(), desM.getText(),
                   a, Integer.parseInt(nbjM.getText()), new ArrayList<>(ex));
            try{

                es.modifier(de);
                EX();
                ImageView commentaireIcon = new ImageView(new Image(getClass().getResourceAsStream("/Front/images/exo/ez.png")));
                commentaireIcon.setFitHeight(50);
                commentaireIcon.setFitWidth(50);
                Notifications.create()
                        .title("Succès")
                        .text("Le defi a été a modifié avec succées !!")
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
            alert.setContentText("Echec de la modification du defi !!");
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