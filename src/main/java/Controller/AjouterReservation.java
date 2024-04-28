package Controller;

import Entities.Reservation;
import Entities.Session;
import Service.ServiceReservation;
import Service.ServiceSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class AjouterReservation {

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
        private ComboBox<String> comboBoxClient;
        @FXML
        private ComboBox<String> comboBoxEtat;
        @FXML
        private ComboBox<String> comboBoxSession;
        @FXML
        private DatePicker datePicker;

        private SessionController sessionController;


        public void initialize(URL location, ResourceBundle resources) {
            etatE.setText("");
            // Initialise la liste déroulante des coaches, si nécessaire
            comboBoxClient.getItems().addAll("Firas", "Rayan", "Yahya", "Aziz"); // Remplacez avec vos propres valeurs si nécessaire

            // Remplacez avec vos types de session réels
            comboBoxSession.getItems().addAll( "hit", "endurance", "cross fit","pilate","Muscu-training"); // Correction du nom de la ComboBox
            comboBoxEtat.getItems().addAll( "1","2");
        }

        @FXML
        private void sauvegarderReservation(ActionEvent actionEvent) {
            boolean test = true;
            // Vérifier si tous les champs de texte sont remplis
            if (comboBoxClient.getValue().isEmpty()) {
                test = false;
               clientE.setText("Client is required");
            } else {
                clientE.setText("");
            }
            if (comboBoxSession.getValue() == null || comboBoxSession.getValue().isEmpty()) {
                test = false;
                sessionE.setText("Session is required");
            } else {
                sessionE.setText("");
            }

            // Vérifier si la date est postérieure à la date actuelle
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate == null || selectedDate.isBefore(LocalDate.now())) {
                dateE.setText("La date doit être supérieure à la date actuelle");
                test = false;
            } else {
                dateE.setText("");
            }

            if (test) {
                saveSessionToDatabase();
                fermerFenetre();
                navigateToMainInterface();
            }
        }
    private void saveSessionToDatabase() {
        String session = comboBoxSession.getValue();
        String client = comboBoxClient.getValue();
        String etat = comboBoxEtat.getValue();
        LocalDate selectedDate = datePicker.getValue();
        Date sqlDate = Date.valueOf(selectedDate);

        // Create a new reservation object
        Reservation reservation = new Reservation(session, sqlDate, etat, client);

        // Call the service to save the reservation
        ServiceReservation serviceReservation = new ServiceReservation();
        try {
            serviceReservation.add(reservation);
            showSuccessMessage("Reservation added successfully!");
            // If needed, notify the session controller to refresh the sessions table
            if (sessionController != null) {
                sessionController.refreshSessions();
            }
        } catch (Exception e) {
            showErrorAlert("Failed to add reservation: " + e.getMessage());
        }
    }



        private void fermerFenetre() {
            // Récupérer la scène actuelle à partir du bouton sauvegarder
            Stage stage = (Stage) btnsave.getScene().getWindow();
            // Fermer la fenêtre AjouterSession
            stage.close();
        }

        private void navigateToMainInterface() {
            try {
                Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/Reservation.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setSessionController(SessionController sessionController) {
            this.sessionController = sessionController;
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

        @FXML
        void createSession() {
            try {
                Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/Reservation.fxml"));
                Stage s;
                Scene scene = new Scene(loader);
                s = (Stage) (comboBoxEtat).getScene().getWindow();
                s.setScene(scene);
                s.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}


