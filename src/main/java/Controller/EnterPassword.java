package Controller;

import Entities.SessionManager;
import Utils.MyDatabase;
import at.favre.lib.crypto.bcrypt.BCrypt;
import helper.AlertHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class EnterPassword implements Initializable {
    @FXML
    private Button changePWD_btn;

    @FXML
    private Label checkOld_PWD;

    @FXML
    private ImageView goback;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField new_pwd;

    @FXML
    private TextField old_pwd;
    Window window ;

    @FXML
    void change_pwd(ActionEvent actionEvent) {
        String passwordnew = new_pwd.getText();
        System.out.println("New password: " + passwordnew);

        if (password()) {
            Connection con = MyDatabase.getConnect();
            String userId = SessionManager.getInstance().getUserId();
            System.out.println("User ID: " + userId);

            try {
                String query = "SELECT pwd FROM user WHERE id = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, userId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String hashedbase = resultSet.getString("pwd");
                    System.out.println("Hashed password from database: " + hashedbase);

                    BCrypt.Result result = BCrypt.verifyer().verify(old_pwd.getText().toCharArray(), hashedbase);
                    if (result.verified) {
                        System.out.println("Old password verified successfully.");

                        String hashedPwd = BCrypt.withDefaults().hashToString(12, new_pwd.getText().toCharArray());
                        System.out.println("New hashed password: " + hashedPwd);

                        String query_edit = "UPDATE user SET pwd = ? WHERE id = ?";
                        PreparedStatement statement1 = con.prepareStatement(query_edit);
                        statement1.setString(1, hashedPwd);
                        statement1.setString(2, userId);
                        int rowsAffected = statement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Password updated successfully.");

                            AlertHelper.showAlert(Alert.AlertType.INFORMATION, window, "Information",
                                    "User password updated successfully, Session expired in 5 seconds");

                            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
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
                            System.out.println("Failed to update password.");
                            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                                    "Failed to edit user password, try again");
                        }
                    } else {
                        System.out.println("Incompatible old password.");
                        AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                                "Incompatible Old password");
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
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

        boolean isValid = true;

        if (old_pwd.getText().isEmpty() ||  new_pwd.getText().isEmpty()) {
            checkOld_PWD.setVisible(true);
            checkOld_PWD.setText("Fields cannot be blank.");
            checkOld_PWD.setStyle("-fx-text-fill: red;");
            isValid = false;
        } else if (!CheckPasswordConstraint(old_pwd.getText()) || !CheckPasswordConstraint(new_pwd.getText())) {
            checkOld_PWD.setVisible(true);
            checkOld_PWD.setText("Password must be strong: at least one uppercase letter, one special character, and one digit.");
            checkOld_PWD.setStyle("-fx-text-fill: red;");
            isValid = false;
        } else {
            checkOld_PWD.setVisible(false);
        }
        return isValid;

    }

    @FXML
    public void goback(MouseEvent actionEvent) throws IOException {
        Stage stage = (Stage) changePWD_btn.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/clientPanne.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User Profile");
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FileInputStream fileInputStream2 = new FileInputStream("C:/Users/bouaz/PREVIOUS/src/main/java/image/gobackwhite.png"); // Replace "path_to_your_image.jpg" with the actual path to your image file
            Image image3 = new Image(fileInputStream2);
            goback.setImage(image3);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
