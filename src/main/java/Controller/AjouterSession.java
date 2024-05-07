package Controller;

import Entities.Session;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AjouterSession implements Initializable {

    @FXML
    private Label typeE;
    @FXML
    private Label capE;
    @FXML
    private Label coachE;
    @FXML
    private Label dateE;
    @FXML
    private Button btnsave;
    @FXML
    private ComboBox<String> comboBoxCoach;
    @FXML
    private ComboBox<String> comboBoxType;
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField tCap;
    private SessionController sessionController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coachE.setText("");
        // Initialise la liste déroulante des coaches, si nécessaire
        comboBoxCoach.getItems().addAll("Firas", "Rayan", "Yahya", "Aziz"); // Remplacez avec vos propres valeurs si nécessaire

        // Remplacez avec vos types de session réels
        comboBoxType.getItems().addAll( "hit", "endurance", "cross fit","pilate","Muscu-training"); // Correction du nom de la ComboBox
    }

    @FXML
    private void AjouterSE(ActionEvent actionEvent) {
        boolean test = true;
        // Vérifier si tous les champs de texte sont remplis
        if (comboBoxType.getValue() == null || comboBoxType.getValue().isEmpty()) {
            test = false;
            typeE.setText("Le type de la session est obligatoire");
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
        // Retrieve data from the form
        String type = comboBoxType.getValue();
        int cap = Integer.parseInt(tCap.getText());
        String coach = comboBoxCoach.getValue();
        // Create a new session object
        java.sql.Date date = java.sql.Date.valueOf(datePicker.getValue());

        Session session = new Session(cap, type, (java.sql.Date) date, coach);

        // Call the service to save the session
        ServiceSession serviceSession = new ServiceSession();
        try {
            serviceSession.add(session);
            showSuccessMessage("Session added successfully!");
            // If needed, notify the session controller to refresh the sessions table
            if (sessionController != null) {
                sessionController.refreshSessions();
            }
        } catch (Exception e) {
            showErrorAlert("Failed to add session: " + e.getMessage());
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
            Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/Sessions-front.fxml"));
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
            Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/AjouterSession.fxml"));
            Stage s;
            Scene scene = new Scene(loader);
            s = (Stage) (comboBoxType).getScene().getWindow();
            s.setScene(scene);
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
