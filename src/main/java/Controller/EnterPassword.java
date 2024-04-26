package Controller;

import Entities.SessionManager;
import Utils.DataBase;
import helper.AlertHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.codec.digest.DigestUtils;
import javafx.stage.Window;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;

public class EnterPassword {
    @FXML
    private Button changePWD_btn;

    @FXML
    private Label checkNew_PWD;

    @FXML
    private Label checkOld_PWD;

    @FXML
    private Label firstname;

    @FXML
    private TextField new_pwd;

    @FXML
    private TextField old_pwd;
    Window window ;

    @FXML
    void change_pwd(ActionEvent actionEvent) {
        String passwordnew = new_pwd.getText();

        if (password()) {
            String oldPassword = old_pwd.getText();
            String hashedOldPwd = DigestUtils.sha1Hex(oldPassword);
            DataBase dataBase = new DataBase();
            Connection con = dataBase.getConnect();
            String userId = SessionManager.getInstance().getUserFront();
            try {
                String query = "SELECT pwd FROM user WHERE id = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, userId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String hashedbase = resultSet.getString("pwd");
                    if (hashedbase.equals(hashedOldPwd)) {
                        if (passwordnew.equals(new_pwd)) {
                            String hashedPwd = DigestUtils.sha1Hex(passwordnew);
                            String query_edit = "UPDATE user SET pwd = ? WHERE id = ?";
                            PreparedStatement statement1 = con.prepareStatement(query_edit);
                            statement1.setString(1, hashedPwd);
                            statement1.setString(2, userId);
                            int rowsAffected = statement1.executeUpdate();
                            if (rowsAffected > 0) {
                                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window, "Information",
                                        "User password updated successfully , Session expired in 10 seconds");
                                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                                    SessionManager.getInstance().cleanUserSessionFront();
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/LogIn.fxml"));
                                    Parent root = null;
                                    try {
                                        root = loader.load();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Stage stage = (Stage) new_pwd.getScene().getWindow();
                                    stage.setScene(new Scene(root));
                                }));
                                timeline.setCycleCount(1);
                                timeline.play();
                            } else {
                                AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                                        "Failed to edit user password, try again");
                            }
                        } else {
                            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                                    "Password and confirm password are not compatible");
                        }
                    } else {
                        AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                                "Incompatible Old password");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        }
    public boolean CheckPasswordConstraint(String test) {
        if (test.length() < 8)
            return false;
        boolean hasUpperCase = false;
        boolean hasSpecialChar = false;
        boolean hasDigit = false;
        String specialCharacters = "!@#$%^&*()-_+=<>?";
        for (int i = 0; i < test.length(); i++) {
            char c = test.charAt(i);
            if (Character.isUpperCase(c))
                hasUpperCase = true;
            if (specialCharacters.contains(String.valueOf(c)))
                hasSpecialChar = true;
            if (Character.isDigit(c))
                hasDigit = true;
        }
        return hasUpperCase && hasSpecialChar && hasDigit;
    }
    private boolean password() {

            boolean verif = true;

            if (old_pwd.getText().isEmpty() || old_pwd.getText().isEmpty() ) {
                checkOld_PWD.setVisible(true);
                checkOld_PWD.setText("Fields cannot be blank.");
                checkOld_PWD.setStyle("-fx-text-fill: red;");
                verif = false;
            } else if (!CheckPasswordConstraint(old_pwd.getText()) || !CheckPasswordConstraint(new_pwd.getText())) {
                checkOld_PWD.setVisible(true);
                checkOld_PWD.setText("Password must be strong: at least one uppercase letter, one special character, and one digit.");
                checkOld_PWD.setStyle("-fx-text-fill: red;");
                verif = false;
            } else {
                checkOld_PWD.setVisible(false);
            }
            return verif;

    }


}
