package Controller;
import Entities.Session;
import Service.ServiceSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class ModifierSession {

    @FXML
    private Label dateE;
    @FXML
    private Label typeE;
    @FXML
    private Label capE;
    @FXML
    private Label coachE;
    @FXML
    private TextField idA;
    @FXML
    private TextField tCap;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> comboBoxCoach;
    @FXML
    private ComboBox<String> comboBoxType;

    private SessionController sessionController;
    private Stage primaryStage;
@FXML
private Button btnsave;
    public void initialize(URL location, ResourceBundle resources) {
        coachE.setText("");
        // Initialise la liste déroulante des coaches, si nécessaire
        comboBoxCoach.getItems().addAll("Firas", "Rayan", "Yahya", "Aziz"); // Remplacez avec vos propres valeurs si nécessaire

        // Remplacez avec vos types de session réels
        comboBoxType.getItems().addAll( "hit", "endurance", "cross fit","pilate","Muscu-training"); // Correction du nom de la ComboBox
    }
    public void setIdA(String idA) {
        this.idA.setText(idA);
    }

    public void settCap(String tCap) {
        this.tCap.setText(tCap);
    }

    public void setDatePicker(LocalDate newDate) {
        this.datePicker.setValue(newDate);
    }
    public void setComboBoxCoach(String selectedCoach) {
        this.comboBoxCoach.setValue(selectedCoach);
    }
    public void setComboBoxType(String selectedType) {
        this.comboBoxType.setValue(selectedType);
    }



    // Méthode appelée lorsque le bouton est cliqué
    public void Se() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Sessions.fxml"));
            primaryStage = (Stage) tCap.getScene().getWindow();
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
    private void ModifierSE(ActionEvent actionEvent) {
        ServiceSession ss = new ServiceSession();
        boolean test = true;
        // Vérifier si tous les champs de texte sont remplis
        if (comboBoxType.getValue().isEmpty()) {
            test = false;
            typeE.setText("vide");
        } else {
            typeE.setText("");
        }

        // Vérifier si la capacité est un entier
        if (!isInteger(tCap.getText())) {
            test = false;
            capE.setText("La capacité de la session doit être un entier");
        } else {
            capE.setText("");
        }

        if (comboBoxCoach.getValue() == null || comboBoxCoach.getValue().isEmpty()) {
            coachE.setText("Coach is required");
            test = false;
        } else {
            coachE.setText("");
        }

        // Vérifier si la date est postérieure à la date actuelle
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null || selectedDate.isBefore(LocalDate.now())) {
            dateE.setText("Date must be in the future");
            test = false;
        } else {
            dateE.setText("");
        }
        
        if (test) {
            ServiceSession see= new ServiceSession();
            Date date = Date.valueOf(selectedDate);
            Session se = new Session(Integer.parseInt(idA.getText()) , Integer.parseInt(tCap.getText()) , comboBoxType.getValue(), date , comboBoxCoach.getValue());
            try {
                see.modifier(se);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("La Session a été a modifié avec succées !!");
                alert.showAndWait();
                Se();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else {

            System.out.println("Veuillez remplir tous les champs.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Echec");
            alert.setContentText("Echec !!");
            alert.showAndWait();
        }
    }
    private void showSuccessMessage(String message) {
        // You can implement this method to show a success message to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    private void fermerFenetre() {
        // Récupérer la scène actuelle à partir du bouton sauvegarder
        Stage stage = (Stage) btnsave.getScene().getWindow();
        // Fermer la fenêtre AjouterSession
        stage.close();
    }
}