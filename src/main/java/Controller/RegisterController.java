package Controller;
import javafx.scene.Node;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import helper.AlertHelper;
import Entities.*;
import Service.*;
import Utils.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.codec.digest.DigestUtils;
import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ResourceBundle;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

import java.util.UUID;
import java.util.regex.Pattern;

public class RegisterController  implements Initializable {

    @FXML
    private Label CheckAdresse;

    @FXML
    private Label CheckMail;

    @FXML
    private Label CheckPhoneNumber;

    @FXML
    private TextField adresse;
    @FXML
    private Button img_btn;

    @FXML
    private ImageView pp_view;
    @FXML
    private ImageView imageView;


    @FXML
    private Label checkPhoto;

    @FXML
    private Label checkRole;
    @FXML
    private Label checkDateBirthday;

    @FXML
    private Label checkPassword;

    @FXML
    private Label checkfirstName;

    @FXML
    private Label checklastName;

    @FXML
    private DatePicker dateBirthday;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;


    @FXML
    private TextField lastName;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phoneNumber;
    @FXML
    private ComboBox<String> role;

    @FXML
    private Button registerButton;
    private String imagePath;
    private AlertHelper alertHelper = new AlertHelper();
    private final Connection con;

    private boolean isAlreadyRegisteredWithMail() {
        PreparedStatement ps;
        ResultSet rs;
        boolean MailExist = false;
        String query = "select * from user WHERE email = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, email.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                MailExist = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return MailExist;
    }

    private boolean isAlreadyRegisteredWithPhoneNumber() {
        PreparedStatement ps;
        ResultSet rs;
        boolean PhoneExist = false;
        String query = "select * from user WHERE num_tel = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, phoneNumber.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                PhoneExist = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return PhoneExist;
    }

    private boolean CheckFirstNameConstraint(String test) {
        if (test.length() < 3) {
            return false;
        }
        for (int i = 0; i < test.length(); i++) {
            char c = test.charAt(i);
            if (!(c >= 'a' && c <= 'z')) {
                return false;
            }
        }
        return true;
    }

    private boolean CheckLastNameConstraint(String test) {
        if (test.length() < 3) {
            return false;
        }
        for (int i = 0; i < test.length(); i++) {
            char c = test.charAt(i);
            if (!(c >= 'a' && c <= 'z')) {
                return false;
            }
        }
        return true;
    }

    private boolean CheckPhoneNumberConstraint(String test) {
        if (test.length() != 8) {
            return false;
        }
        for (int i = 0; i < test.length(); i++) {
            char c = test.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        role.getItems().addAll("Client", "Coach");
        try {
            FileInputStream fileInputStream1 = new FileInputStream("C:/Users/bouaz/PREVIOUS/src/main/java/image/logo.png"); // Replace "path_to_your_image.jpg" with the actual path to your image file
            Image image2 = new Image(fileInputStream1);
            imageView.setImage(image2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileInputStream2 = new FileInputStream("C:/Users/bouaz/PREVIOUS/src/main/java/image/blank-profile-picture-973460_1280.png"); // Replace "path_to_your_image.jpg" with the actual path to your image file
            Image image3 = new Image(fileInputStream2);
            pp_view.setImage(image3);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public boolean CheckPasswordConstraint(String test) {


        // Check if the password length is at least 8 characters
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

    private boolean checkIsValidated() {
        boolean verif = true;
        if (firstName.getText().isEmpty()) {
            checkfirstName.setVisible(true);
            checkfirstName.setText("First name field cannot be blank.");
            checkfirstName.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckFirstNameConstraint(firstName.getText())) {
            checkfirstName.setVisible(true);
            checkfirstName.setText("First name must contain only characters [a-z], minimum length is 3");
            checkfirstName.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checkfirstName.setVisible(false);
        }

        if (lastName.getText().isEmpty()) {
            checklastName.setVisible(true);
            checklastName.setText("Last name field cannot be blank.");
            checklastName.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckLastNameConstraint(lastName.getText())) {
            checklastName.setVisible(true);
            checklastName.setText("Last name must contain only characters [a-z], minimum length is 3");
            checklastName.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checklastName.setVisible(false);
        }

        if (phoneNumber.getText().isEmpty()) {
            CheckPhoneNumber.setVisible(true);
            CheckPhoneNumber.setText("Phone Number field cannot be blank.");
            CheckPhoneNumber.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckPhoneNumberConstraint(phoneNumber.getText())) {
            CheckPhoneNumber.setVisible(true);
            CheckPhoneNumber.setText("The telephone number must consist of exactly 8 digits.");
            CheckPhoneNumber.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (isAlreadyRegisteredWithPhoneNumber()) {
            CheckPhoneNumber.setVisible(true);
            CheckPhoneNumber.setText("Phone Number already exists");
            CheckPhoneNumber.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            CheckPhoneNumber.setVisible(false);
        }
        if (pp_view.getImage() == null) {
            checkPhoto.setVisible(true);
            checkPhoto.setText("Photo field cannot be blank.");
            checkPhoto.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checkPhoto.setVisible(false);
        }
        if (adresse.getText().isEmpty()) {
            CheckAdresse.setVisible(true);
            CheckAdresse.setText("Address field cannot be blank.");
            CheckAdresse.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            CheckAdresse.setVisible(false);
        }
        if (email.getText().isEmpty()) {
            CheckMail.setVisible(true);
            CheckMail.setText("Mail field cannot be blank.");
            CheckMail.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (isAlreadyRegisteredWithMail()) {
            CheckMail.setVisible(true);
            CheckMail.setText("Mail already exists");
            CheckMail.setStyle("-fx-text-fill: red;");
        } else if (!isValidEmail(email.getText())) {
            CheckMail.setVisible(true);
            CheckMail.setText("Invalid email format.");
            CheckMail.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            CheckMail.setVisible(false);
        }


        if (role.getValue() == null) {
            checkRole.setVisible(true);
            checkRole.setText("role field cannot be blank.");
            checkRole.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checkRole.setVisible(false);
        }
        if (dateBirthday.getValue() == null) {
            checkDateBirthday.setVisible(true);
            checkDateBirthday.setText("Birthday Date field cannot be blank.");
            checkDateBirthday.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checkDateBirthday.setVisible(false);
        }
        if (password.getText().isEmpty()) {
            checkPassword.setVisible(true);
            checkPassword.setText("Password field cannot be blank.");
            checkPassword.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckPasswordConstraint(password.getText())) {
            checkPassword.setVisible(true);
            checkPassword.setText("Password must be strong: at least one uppercase letter, one special character, and one digit.");
            checkPassword.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checkPassword.setVisible(false);
        }


        if (!verif) {
            return false;
        }
        return true;

    }

    public RegisterController() {
        DataBase dataBase = new DataBase();
        con = dataBase.getConnect();
    }

    private String generateUniqueToken() {
        // Generate a random UUID (Universally Unique Identifier)
        return UUID.randomUUID().toString();
    }



    public void viderTextField() {
        firstName.clear();
        lastName.clear();
        email.clear();
        adresse.clear();
        password.clear();
        phoneNumber.clear();
        role.getSelectionModel().clearSelection();
        dateBirthday.valueProperty().setValue(null);

    }
    @FXML
    void add_img(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String imagePath = file.toURI().toString();
            System.out.println("Image Path: " + imagePath); // Debugging: Print out image path

            try {
                Image image = new Image(imagePath);
                pp_view.setImage(image);
                // Set the image to the ImageView
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error loading image: " + e.getMessage());
                alert.show();
            }

        }
        imagePath = file.getAbsolutePath();
    }
    @FXML
    public void register(ActionEvent actionEvent) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String token = generateUniqueToken();
        if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || password.getText().isEmpty() || adresse.getText().isEmpty() || email.getText().isEmpty() || role.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all the required fields.");
            alert.showAndWait();
            // Exit the method if any required field is empty
        }
        if (checkIsValidated()) {

            UserService us = new UserService();
            Window window = registerButton.getScene().getWindow();
            String nom = firstName.getText();
            String prenom = lastName.getText();
            String mail = email.getText();
            String tel = phoneNumber.getText();
            String pwd = password.getText();


            LocalDate date_n = dateBirthday.getValue();
            String roles = role.getValue();
            String adress = adresse.getText();


            String hashedPwd = BCrypt.withDefaults().hashToString(12, pwd.toCharArray());
            User user = new User(nom, prenom, mail,hashedPwd,date_n,roles,adress, imagePath, tel);
            boolean isUserAdded = us.addUser(user);
            AlertHelper AlertHelper = null;
            if (isUserAdded) {
                String query = "SELECT id FROM user ORDER BY id DESC LIMIT 1";
                try {
                    ps = con.prepareStatement(query);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        int userID = rs.getInt("id");
                            String subject = "Welcome On our BreatheOut Application";
                        String verificationUrl = "http://www.breatheout.com/user/verif/email/" + userID + "/" + token;
                        String messageBody = "Welcome to Breathe Out!\n\n";
                        messageBody += "Please click the following link to verify your account:\n";
                        messageBody += "<a href='" + verificationUrl + "'>Verify Account</a>";
                        String email_to = email.getText();
                        EmailSender.sendEmail(email_to, subject, messageBody);
                        viderTextField();
                        AlertHelper.showAlert(Alert.AlertType.INFORMATION, window, "Information",
                                "You have registered successfully.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                            "Failed to retrieve user ID. Please try again later.");
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                        "Failed to register user. Please try again later.");
            }
        }
    }

    @FXML
    public void showLoginStage(MouseEvent mouseEvent)  throws IOException{
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/LogIn.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User LogIn");
        stage.show();
    }

}

