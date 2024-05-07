package Controller;

import Entities.Reservation;
import Service.ServiceReservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

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

        private ReservationController reservationController;


        public void initialize() {
            etatE.setText("");
            // Initialise la liste déroulante des coaches, si nécessaire
            comboBoxClient.getItems().addAll("Firas", "Rayan", "Yahya", "Aziz"); // Remplacez avec vos propres valeurs si nécessaire

            // Remplacez avec vos types de session réels
            comboBoxSession.getItems().addAll("1","2","3","4","5"); // Correction du nom de la ComboBox
            comboBoxEtat.getItems().addAll( "1","2");
        }

        @FXML
        private void sauvegarderReservation(ActionEvent actionEvent) {
            boolean test = true;
            // Vérifier si tous les champs de texte sont remplis
            if  (comboBoxSession.getValue() == null ||comboBoxClient.getValue().isEmpty()) {
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
            if (comboBoxEtat.getValue() == null || comboBoxEtat.getValue().isEmpty()) {
                test = false;
                etatE.setText("Etat is required");
            } else {
                etatE.setText("");
            }
            if (test) {
                saveSessionToDatabase();
                fermerFenetre();
                navigateToMainInterface();
            }
        }
    private void saveSessionToDatabase() {
            String client = comboBoxClient.getValue();
        String etat = comboBoxEtat.getValue();
        String session = comboBoxSession.getValue();
        java.sql.Date date = java.sql.Date.valueOf(datePicker.getValue());

        // Create a new reservation object
        Reservation reservation = new Reservation(session , (java.sql.Date) date , etat,client);

        // Call the service to save the reservation
        ServiceReservation serviceReservation = new ServiceReservation();
        try {
            serviceReservation.add(reservation);
            showSuccessMessage("Reservation added successfully!");
            // If needed, notify the session controller to refresh the sessions table
            if (reservationController != null) {
                reservationController.refreshReservations();
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
                Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/Reservation-front.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
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

        @FXML
        void createSession() {
            try {
                Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/Reservation-front.fxml"));
                Stage s;
                Scene scene = new Scene(loader);
                s = (Stage) (comboBoxEtat).getScene().getWindow();
                s.setScene(scene);
                s.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    private String getSessionType(String sessionNumber) {
        switch (sessionNumber) {
            case "1":
                return "hit";
            case "2":
                return "endurance";
            case "3":
                return "cross fit";
            case "4":
                return "pilate";
            default:
                return "Muscu-training";
        }
    }
}


