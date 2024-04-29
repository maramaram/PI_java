package Controller;
import Entities.SessionManager;
import helper.AlertHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import Service.*;
import Utils.DataBase;
import javafx.stage.Window;
import com.twilio.Twilio;
import javafx.util.Duration;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgotPwdController implements Initializable {


    @FXML
    private Label CheckPhoneNumber;

    @FXML
    private Button CodeButton1;

    @FXML
    private Label checkPasswordForgot;

    @FXML
    private Label checkcode;

    @FXML
    private Label checkconfirmpassword;

    @FXML
    private AnchorPane code;

    @FXML
    private TextField codefield;

    @FXML
    private TextField confirmPassword;

    @FXML
    private ImageView copy;

    @FXML
    private ImageView goback;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField newpasswordfield;

    @FXML
    private AnchorPane password;

    @FXML
    private TextField phone_number;

    @FXML
    private Button resetbutton;

    @FXML
    private Button sendCode_btn;

    @FXML
    private AnchorPane telephone;
    private final Connection con;
    Window window;
    private String codeFromSMS;



    public ForgotPwdController() {
        DataBase dataBase = new DataBase();
        this.con = dataBase.getConnect();
    }
    private boolean isPhoneNumberAvailable(String phoneNumber) throws SQLException {
        String query = "SELECT * FROM user WHERE num_tel = ?";
        PreparedStatement preparedStatement = DataBase.getConnect().prepareStatement(query);
        preparedStatement.setString(1, phoneNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();

    }
    @FXML
    void checkCODE(ActionEvent event) {
        code.setVisible(false);
        password.setVisible(true);
        telephone.setVisible(false);
        String codeEntered = codefield.getText();
        if (codeEntered.equals(codeFromSMS)) {
            checkcode.setText("Code is correct");
            checkcode.setStyle("-fx-text-fill: green");
       password.setVisible(true);
            checkcode.setVisible(false);
        } else {
            checkcode.setText("Code is incorrect");
            checkcode.setStyle("-fx-text-fill: red");
            checkcode.setVisible(true);
        }
    }
    @FXML
    void sendCode(ActionEvent event)throws SQLException {
        String num = phone_number.getText();
        if (isPhoneNumberAvailable(num)) {
            String query = "SELECT nom, prenom FROM user WHERE num_tel = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String fullName = resultSet.getString("nom") + " " + resultSet.getString("prenom");
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window, "Information",
                        "Check your phone for your code");
                Random random = new Random();
                int coder = random.nextInt(10000);
                codeFromSMS = String.format("%04d", coder);
                code.setVisible(true);
               password.setVisible(false);
               telephone.setVisible(false);
                final String ACCOUNT_SID = "AC826dd4583d21e1d761edfdbefca0daf8";
                final String AUTH_TOKEN = "6dfb8cfec4006bd5b1df833c60c9eca2";
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                Message message = Message.creator(
                                new PhoneNumber("+21655614560"),
                                new PhoneNumber("+14845529358"),
                                "Hi " + fullName + " , this is your code for password reset: " + codeFromSMS)
                        .create();
            }
        } else {
            CheckPhoneNumber.setText("Invalid phone number");
            CheckPhoneNumber.setStyle("-fx-text-fill: red");
            CheckPhoneNumber.setVisible(true);
        }
    }

    @FXML
    public void go_to_login(MouseEvent actionEvent) throws IOException {
        Stage stage = (Stage) sendCode_btn.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/LogIn.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User LogIn");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        code.setVisible(false);
        password.setVisible(false);
        telephone.setVisible(true);
        try {
            // Convert the file path to a URL
            File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/goback.png");
            String imageUrl = file.toURI().toURL().toString();
            // Create an Image object from the URL
            Image image = new Image(imageUrl);
            // Set the image to the ImageView
            goback.setImage(image);
        } catch (MalformedURLException e) {
            // Handle invalid URL exception
            e.printStackTrace();
            // Optionally, show an alert or fallback image
        }
        try {
            // Convert the file path to a URL
            File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/logo.png");
            String imageUrl = file.toURI().toURL().toString();
            // Create an Image object from the URL
            Image image = new Image(imageUrl);
            // Set the image to the ImageView
            imageView.setImage(image);
        } catch (MalformedURLException e) {
            // Handle invalid URL exception
            e.printStackTrace();
            // Optionally, show an alert or fallback image
        }

    }
    @FXML
    void resetbutton(ActionEvent event) throws SQLException {
        String newPassword = newpasswordfield.getText();
        String ConfirmPassword = confirmPassword.getText();
        if (checkPasswordForgot()) {
            if (newPassword.equals(ConfirmPassword)) {
                String query = "UPDATE user SET pwd = ? WHERE num_tel = ?";
                PreparedStatement preparedStatement = DataBase.getConnect().prepareStatement(query);
                String hashedPwd = DigestUtils.sha1Hex(newPassword);
                String hashedConfirmPwd = DigestUtils.sha1Hex(ConfirmPassword);
                preparedStatement.setString(1, hashedPwd);
                preparedStatement.setString(2, phone_number.getText());
                preparedStatement.executeUpdate();
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window, "Information",
                        "User password updated successfully");
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                        "Password and confirm password are not compatible");
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
    public boolean checkPasswordForgot()
    {  code.setVisible(false);
        password.setVisible(true);
        telephone.setVisible(false);
        boolean verif = true;
        if (newpasswordfield.getText().isEmpty() || confirmPassword.getText().isEmpty()) {

            checkPasswordForgot.setText("Fields cannot be blank.");
            checkPasswordForgot.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckPasswordConstraint(newpasswordfield.getText()) || !CheckPasswordConstraint(confirmPassword.getText())) {

            checkPasswordForgot.setText("Password must be strong: at least one uppercase letter, one special character, and one digit.");
            checkPasswordForgot.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checkPasswordForgot.setVisible(false);
        }
        return verif;
    }

    public void showSignInStage() throws IOException {
        Stage stage = (Stage) sendCode_btn.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/SignUp.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User SignIn");
        stage.show();
    }public void goback() throws IOException {
        Stage stage = (Stage) sendCode_btn.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/LogIn.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log IN");
        stage.show();
    }
}
