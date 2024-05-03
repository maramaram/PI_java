package Controller;

import Entities.Reservation;
import Entities.Session;
import Service.ServiceReservation;
import Service.ServiceSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class ModifierReservation {
    @FXML
    private ComboBox<String> comboBoxClient;
    @FXML
    private ComboBox<String> comboBoxEtat;
    @FXML
    private ComboBox<String> comboBoxSession;

    @FXML
    private Label etatE;
    @FXML
    private Label sessionE;
    @FXML
    private Label clientE;
    @FXML
    private Label dateE;
    @FXML
    private Button btnsave;
    @FXML
    private TextField idA;

    @FXML
    private DatePicker datePicker;

    private ReservationController reservationControllerController;
    private Stage primaryStage;

    public void initialize() {
        etatE.setText("");
        // Initialise la liste déroulante des coaches, si nécessaire
        comboBoxClient.getItems().addAll("Firas", "Rayan", "Yahya", "Aziz"); // Remplacez avec vos propres valeurs si nécessaire

        // Remplacez avec vos types de session réels
        comboBoxSession.getItems().addAll("1","2","3","4","5"); // Correction du nom de la ComboBox
        comboBoxEtat.getItems().addAll( "1","2");
    }

    public void setIdA(String idA) {
        this.idA.setText(idA);
    }

    public void setDatePicker(LocalDate newDate) {
        this.datePicker.setValue(newDate);
    }
    public void setComboBoxClient(String selectedClient) {
        this.comboBoxClient.setValue(selectedClient);
    }
    public void setComboBoxSession(String selectedSession) {
        this.comboBoxSession.setValue(selectedSession);
    }
    public void setComboBoxEtat (String selectedEtat) { this.comboBoxEtat.setValue(selectedEtat);}

    public void Se() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Reservation.fxml"));
            primaryStage = (Stage) comboBoxEtat.getScene().getWindow();
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
        ServiceReservation sr = new ServiceReservation();
        boolean test = true;
        // Vérifier si tous les champs de texte sont remplis
        if (comboBoxEtat.getValue().isEmpty()) {
            test = false;
            etatE.setText("vide");
        } else {
            etatE.setText("");
        }

        if(comboBoxClient.getValue().isEmpty() || comboBoxClient.getValue() == null) {
    test = false;
    clientE.setText("vide");
} else {
    clientE.setText("");
}

        if (comboBoxSession.getValue() == null || comboBoxSession.getValue().isEmpty()) {
            sessionE.setText("Session is required");
            test = false;
        } else {
            sessionE.setText("");
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
            ServiceReservation ree= new ServiceReservation();
            Date date = Date.valueOf(selectedDate);
            Reservation re = new Reservation(comboBoxEtat.getValue() , comboBoxClient.getValue(), comboBoxSession.getValue(),date);
            try {
                ree.modifier(re);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succées");
                alert.setContentText("La Reservation a été a modifié avec succées !!");
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

    private void fermerFenetre() {
        // Récupérer la scène actuelle à partir du bouton sauvegarder
        Stage stage = (Stage) btnsave.getScene().getWindow();
        // Fermer la fenêtre ModifierSession
        stage.close();
    }
}
