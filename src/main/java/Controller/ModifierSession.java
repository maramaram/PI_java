package Controller;

import Entities.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifierSession implements Initializable {

    @FXML
    private TextField tCap;

    @FXML
    private TextField tDes;

    @FXML
    private TextField tType;

    @FXML
    private ComboBox<String> comboBoxCoach;
    @FXML
    private Button btnsave;

    private Session sessionToModify;
    private SessionController sessionController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxCoach.getItems().addAll("Firas", "Coach 2", "Coach 3");
    }

    public void setSession(Session session) {
        sessionToModify = session;
        if (sessionToModify != null) {
            tCap.setText(String.valueOf(sessionToModify.getCap()));
            tDes.setText(sessionToModify.getDes());
            tType.setText(sessionToModify.getType());
            comboBoxCoach.setValue(sessionToModify.getCoach());
        }
    }

    public void setSessionController(SessionController controller) {
        this.sessionController = controller;
    }
    @FXML
    private void sauvegarderSession(ActionEvent event) {
        if (sessionToModify != null) {
            sessionToModify.setCap(Integer.parseInt(tCap.getText()));
            sessionToModify.setDes(tDes.getText());
            sessionToModify.setType(tType.getText());
            sessionToModify.setCoach(comboBoxCoach.getValue());

            if (sessionController != null) {
                sessionController.updateSession(sessionToModify);
            }

            // Fermer la fenêtre de modification
            Stage stage = (Stage) tCap.getScene().getWindow();
            stage.close();
        }
    }
    public Session getSessionFromFields() {
        int cap = Integer.parseInt(tCap.getText());
        String des = tDes.getText();
        String type = tType.getText();
        String coach = comboBoxCoach.getValue();


        Session updatedSession = new Session();
        updatedSession.setCap(cap);
        updatedSession.setDes(des);
        updatedSession.setType(type);
        updatedSession.setCoach(coach);
        return updatedSession;
    }
}
