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
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class AjouterSession implements Initializable {

    @FXML
    private Label typeE;
    @FXML
    private Label capE;
    @FXML
    private Label desE;
    @FXML
    private Label coachE;
    @FXML
    private Label dateE;
    @FXML
    private Button btnsave;
    @FXML
    private ComboBox<String> comboBoxCoach;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField tType;
    @FXML
    private TextField tCap;
    @FXML
    private TextField tDes;
    private SessionController sessionController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coachE.setText("");
        // Initialise la liste déroulante des coaches, si nécessaire
        comboBoxCoach.getItems().addAll("Firas", "Rayan", "Coach 3", "Aziz"); // Remplacez avec vos propres valeurs si nécessaire
    }

    @FXML
    private void sauvegarderSession(ActionEvent actionEvent) {
        boolean test = true;
        // Vérifier si tous les champs de texte sont remplis
        if (tType.getText().isEmpty()) {
            test = false;
            typeE.setText("vide");
        } else {
            typeE.setText("");
        }

        // Vérifier si la capacité est un entier
        if (!isInteger(tCap.getText())) {
            test = false;
            capE.setText("Doit être un entier");
        } else {
            capE.setText("");
        }

        if (tDes.getText().isEmpty()) {
            test = false;
            desE.setText("vide");
        } else {
            desE.setText("");
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
        String type = tType.getText();
        int cap = Integer.parseInt(tCap.getText());
        String des = tDes.getText();
        String coach = comboBoxCoach.getValue();
        java.util.Date date = java.util.Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Create a new session object
        Session session = new Session(cap, des, type, date, coach);

        // Call the service to save the session
        ServiceSession serviceSession = new ServiceSession();
        try {
            serviceSession.ajouter(session);
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
            Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/Sessions.fxml"));
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
            Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/Sessions.fxml"));
            Stage s;
            Scene scene = new Scene(loader);
            s = (Stage) (tType).getScene().getWindow();
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
