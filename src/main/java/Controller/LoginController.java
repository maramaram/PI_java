package Controller;

import Entities.SessionManager;
import Entities.User;
import Service.UserService;
import Utils.MyDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.Connection;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import helper.AlertHelper;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.stage.Window;

public class LoginController implements Initializable {
    private final Connection con;
    Window window ;
    @FXML
    private Label LabelCopyNotify;
    @FXML
    private ImageView copy;
    @FXML
    private Label captchaLabel;

    @FXML
    private Label checkrecaptcha;
    @FXML
    private TextField tfCaptcha;
    @FXML
    private ImageView generateCap;
    @FXML
    private ImageView eyehide;
    @FXML
    private ImageView imageView;
    @FXML
    private Label CheckPasswordLogin;

    @FXML
    private Label checkMailLogin;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordFieldLogin;

    @FXML
    private TextField mailFieldLogin;
    private boolean passwordVisible = false;
    private String originalPassword;
    @FXML
    private TextField tempPasswordField;
    private String maskPassword(String password) {
        return "•".repeat(password.length());
    }
    @FXML
    void SHOWpassword(MouseEvent event) {
        if (passwordVisible) {
            passwordFieldLogin.setVisible(true); // Show the original password field
            tempPasswordField.setVisible(false); // Hide the temporary password field
            eyehide.setOpacity(0.3); // Set eye icon opacity to partially transparent
        } else {
            passwordFieldLogin.setVisible(false); // Hide the original password field
            tempPasswordField.setVisible(true); // Show the temporary password field
            tempPasswordField.setText(passwordFieldLogin.getText()); // Set the temporary password field text
            eyehide.setOpacity(1.0); // Set eye icon opacity to fully visible
        }

        // Invert the visibility flag
        passwordVisible = !passwordVisible;

    }

    private void openAdminPanel() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/AdminPannel.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Admin Panel");
        stage.show();
    }

    private void openCoachPanel() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/HomeOn.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Coach Panel");
        stage.show();
    }

    private void openClientPanel() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/HomeOn.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Client Panel");
        stage.show();
    }
    public void login() {
        if (checkIsValidated()) {
            UserService us= new UserService();
            String email = mailFieldLogin.getText();
            String password =passwordFieldLogin.getText();
            User user= us.login(email,password);
            if (user != null) {
                SessionManager.getInstance().setUserId(user.getId());
                String role = user.getRole();
                String status = user.getStatus();
                if ("admin".equals(role)) {
                    String fullName = user.getNom() + " " + user.getPrenom();
                    SessionManager.getInstance().setUserId(fullName);
                    try {
                        openAdminPanel();
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle the IOException appropriately
                    }
                } else if ("coach".equals(role)) {
                    if ("active".equals(status)) {
                        try {
                            openCoachPanel();
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle the IOException appropriately
                        }
                    } else {
                        AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                                "User is not activated yet.");
                    }
                } else if ("Client".equals(role)) {
                    if ("active".equals(status)) {
                        try {
                            openClientPanel();
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle the IOException appropriately
                        }
                    } else {
                        AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                                "User is not activated yet.");
                    }
                }
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                        "Invalid username and password.");
            }
        }
    }

  /*  public void login() {
        if (checkIsValidated()) {
            PreparedStatement ps;
            ResultSet rs;
            String hashedPwd = DigestUtils.sha1Hex(passwordFieldLogin.getText());
            String query = "SELECT * FROM user WHERE email = ? AND pwd = ?";
            try {
                ps = con.prepareStatement(query);
                ps.setString(1, mailFieldLogin.getText());
                ps.setString(2, hashedPwd);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String role = rs.getString("role");
                    String status = rs.getString("status");
                    if ("ADMIN".equals(role)) {
                        String nom= rs.getString("nom") ;
                        String prenom = rs.getString("prenom");
                        String fullName = nom + " " + prenom ;
                        SessionManager.getInstance().setUserId(fullName);
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.close();
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdminPannel.fxml")));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Admin Panel");
                        stage.show();
                    }
                    else if("coach".equals(role)) {
                        if ("active".equals(status))
                        {
                            Stage stage = (Stage) loginButton.getScene().getWindow();
                            stage.close();
                            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CoachPannel.fxml")));
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.setTitle("Coach Panel");
                            stage.show();
                        }
                        else {
                            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                                    "User is not activated yet.");
                        }
                    }
                    else if("Client".equals(role)) {
                        if ("active".equals(status))
                        {
                            Stage stage = (Stage) loginButton.getScene().getWindow();
                            stage.close();
                            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/SignUp.fxml")));
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.setTitle("Client Panel");
                            stage.show();
                        }
                        else {
                            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                                    "User is not activated yet.");
                        }
                    }
                } else {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                            "Invalid username and password.");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }*/

    public LoginController() {
        con = MyDatabase.getConnect();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkMailLogin.setVisible(false);
        CheckPasswordLogin.setVisible(false);
        checkrecaptcha.setVisible(false);
        LabelCopyNotify.setVisible(false);
        generateCaptcha();
        originalPassword = passwordFieldLogin.getText();
        // Hide the temporary password field initially
        tempPasswordField.setVisible(false);

        // Add listener to synchronize changes from the visible password field to the hidden one
        passwordFieldLogin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!passwordVisible) {
                tempPasswordField.setText(newValue);
            }
        });

        // Add listener to synchronize changes from the hidden password field to the visible one
        tempPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (passwordVisible) {
                passwordFieldLogin.setText(newValue);
            }
        });
        try {
            // Convert the file path to a URL
            File file = new File("C:/Users/MSI/Desktop/PI_java-master/src/main/java/image/image-removebg-preview.png");
            String imageUrl = file.toURI().toURL().toString();
            // Create an Image object from the URL
            Image image = new Image(imageUrl);
            // Set the image to the ImageView
            generateCap.setImage(image);
        } catch (MalformedURLException e) {
            // Handle invalid URL exception
            e.printStackTrace();
            // Optionally, show an alert or fallback image
        }

        try {
            FileInputStream fileInputStream = new FileInputStream("C:/Users/MSI/Desktop/PI_java-master/src/main/java/image/image-removebg-preview (1).png"); // Replace "path_to_your_image.jpg" with the actual path to your image file
            Image image1 = new Image(fileInputStream);
            copy.setImage(image1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileInputStream1 = new FileInputStream("C:/Users/MSI/Desktop/PI_java-master/src/main/java/image/logo.png"); // Replace "path_to_your_image.jpg" with the actual path to your image file
            Image image2 = new Image(fileInputStream1);
            imageView.setImage(image2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileInputStream2 = new FileInputStream("C:/Users/MSI/Desktop/PI_java-master/src/main/java/image/image-removebg-preview (2).png"); // Replace "path_to_your_image.jpg" with the actual path to your image file
            Image image3 = new Image(fileInputStream2);
            eyehide.setImage(image3);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void CopyCaptchaCode() {
        copy.setOnMouseClicked(event -> {
            tfCaptcha.setText(captchaLabel.getText());
            LabelCopyNotify.setVisible(true);
            LabelCopyNotify.setText("Copy !");
            LabelCopyNotify.setTextFill(Color.GREEN);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            LabelCopyNotify.setVisible(false);
                        }
                    },
                    4000
            );
        });
    /*
    tfCaptcha.setText(captchaLabel.getText());
    LabelCopyNotify.setVisible(true);
    LabelCopyNotify.setText("Copy !") ;
    Notifications.create()
            .title("Text Copied")
            .text("Captcha code copied successfully")
            .showInformation();

   */
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

    private void clearForm() {
        mailFieldLogin.clear();
        passwordFieldLogin.clear();

    }

    private boolean checkIsValidated() {
        boolean verif = true ;
        if (mailFieldLogin.getText().isEmpty()) {
            checkMailLogin.setVisible(true);
            checkMailLogin.setText("Mail field cannot be blank.");
            checkMailLogin.setStyle("-fx-text-fill: red;");
            verif = false;
        }else if (!isValidEmail(mailFieldLogin.getText())) {
            checkMailLogin.setVisible(true);
            checkMailLogin.setText("Invalid email format.");
            checkMailLogin.setStyle("-fx-text-fill: red;");
            verif = false;
        }else {
            checkMailLogin.setVisible(false);
        }
        if (passwordFieldLogin.getText().isEmpty()) {
            CheckPasswordLogin.setVisible(true);
            CheckPasswordLogin.setText("Password field cannot be blank.");
            CheckPasswordLogin.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckPasswordConstraint(passwordFieldLogin.getText())) {
            CheckPasswordLogin.setVisible(true);
            CheckPasswordLogin.setText("Password must be strong: at least one uppercase letter, one special character, and one digit.");
            CheckPasswordLogin.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            CheckPasswordLogin.setVisible(false);
        }
        if (!tfCaptcha.getText().equals(captchaLabel.getText()))
        {
            checkrecaptcha.setVisible(true);
            checkrecaptcha.setText("incorrect code , try again");
            checkrecaptcha.setStyle("-fx-text-fill: red;");
            verif = false;
        }
        else {
            checkrecaptcha  .setVisible(false);
        }
        return verif;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    public void showSignInStage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/SignUp.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User SignIn");
        stage.show();
    }
    @FXML
    void go_to_forgottenPWD(MouseEvent event) throws IOException {

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/Forgot_pwd.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Mot de passe oublié");
        stage.show();

    }
    @FXML
    private void generateCaptcha() {
        String captcha = generateRandomString(6);
        captchaLabel.setText(captcha);
    }
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            captcha.append(characters.charAt(random.nextInt(characters.length())));
        }
        return captcha.toString();
    }


}