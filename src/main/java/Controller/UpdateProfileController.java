package Controller;

import Entities.SessionManager;
import Entities.User;
import Service.UserService;
import Utils.MyDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;

public class UpdateProfileController {


    @FXML
    private Label CheckMail;

    @FXML
    private Label CheckPhoneNumber;

    @FXML
    private Label checkDateBirthday;

    @FXML
    private Label CheckAdresse;

    @FXML
    private Label checkfirstName;

    @FXML
    private Label checklastName;

    @FXML
    private TextField adress;



    @FXML
    private ImageView goback;




    @FXML
    private DatePicker birthdate;

    @FXML
    private Label logout_btn;

    @FXML
    private ImageView profile_img;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_firstname;

    @FXML
    private TextField tf_lastname;

    @FXML
    private TextField tf_numtel;

    @FXML
    private Button update_btn;
    @FXML
    private ImageView pp_view;
    private Connection con;
    String photoPath;
    private Stage primaryStage;
    private UserService userService;

    public UpdateProfileController() {
        this.userService = new UserService();
        con = MyDatabase.getConnect();
    }

    @FXML
    void choose_photo(ActionEvent event) {
        String userId = SessionManager.getInstance().getUserId();
        UserService userService = new UserService();
        User user = userService.afficher(userId);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        // Filtrer les types de fichiers
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.bmp", "*.jpeg"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        // Afficher la boîte de dialogue pour sélectionner un fichier
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        // Vérifier si un fichier a été sélectionné
        if (selectedFile != null) {
            try {
                // Définir le répertoire de destination dans les ressources
                String destinationDirectory = "Front/images/exo/";
                String fileName = selectedFile.getName();

                // Obtenir le répertoire de destination dans les ressources
                Path destinationPath = Paths.get("src/main/resources", destinationDirectory);

                // Créer le chemin complet du fichier de destination
                Path destinationFilePath = destinationPath.resolve(fileName);

                // Copier le fichier sélectionné vers le répertoire de destination dans les ressources
                Files.copy(selectedFile.toPath(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour le chemin de l'image dans imgA
                String newImagePath = destinationDirectory + fileName;
                user.setPhoto(newImagePath);
                photoPath = user.getPhoto();

                if (photoPath != null && !photoPath.isEmpty()) {

                    Image image = new Image(getClass().getResourceAsStream("/"+photoPath));
                    profile_img.setImage(image);

                }
                // Afficher un message de succès
                System.out.println("Fichier téléchargé avec succès dans les ressources. Nouveau chemin : " + newImagePath);
            } catch (IOException ex) {
                // Gérer les exceptions en cas d'erreur de téléchargement
                ex.printStackTrace();
            }
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }

    }


    @FXML
    void initialize() {
        // Retrieve the current user details from the session manager
       String userId = SessionManager.getInstance().getUserId();
        UserService userService = new UserService();
        User user = userService.afficher(userId);

        // Populate the text fields with the user details
        if (user != null) {
            tf_email.setText(user.getEmail());
            tf_firstname.setText(user.getNom());
            tf_lastname.setText(user.getPrenom());
            tf_numtel.setText(user.getNum_tel());
            adress.setText(user.getAdress());
            birthdate.setValue(user.getDate_N());

            this.goback.setImage(new Image(this.getClass().getResourceAsStream("/images/goback.png")));



            // Load the profile image into profile_img ImageView
            photoPath = user.getPhoto();
            if (photoPath != null && !photoPath.isEmpty()) {

                    Image image = new Image(getClass().getResourceAsStream("/"+photoPath));
                    profile_img.setImage(image);

            }

        }

    }
    @FXML
    public void goback(MouseEvent actionEvent) throws IOException {
        Stage stage = (Stage) update_btn.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/clientPanne.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User Profile");
        stage.show();
    }
    private boolean isAlreadyRegisteredWithMail() {
        PreparedStatement ps;
        ResultSet rs;
        boolean MailExist = false;
        String query = "select * from user WHERE email = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, tf_email.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                MailExist = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return MailExist;
    }
    @FXML
    void logout_click(MouseEvent event) {
        SessionManager.getInstance().cleanUserSessionAdmin();
        try {
            Node sourceNode = (Node) logout_btn;
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.close();
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean isAlreadyRegisteredWithPhoneNumber() {
        PreparedStatement ps;
        ResultSet rs;
        boolean PhoneExist = false;
        String query = "select * from user WHERE num_tel = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, tf_numtel.getText());
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
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
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
    private boolean checkIsValidated() {
        boolean verif = true;
        if (tf_firstname.getText().isEmpty()) {
            checkfirstName.setVisible(true);
            checkfirstName.setText("First name field cannot be blank.");
            checkfirstName.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckFirstNameConstraint(tf_firstname.getText())) {
            checkfirstName.setVisible(true);
            checkfirstName.setText("First name must contain only characters [a-z], minimum length is 3");
            checkfirstName.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checkfirstName.setVisible(false);
        }

        if (tf_lastname.getText().isEmpty()) {
            checklastName.setVisible(true);
            checklastName.setText("Last name field cannot be blank.");
            checklastName.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckLastNameConstraint(tf_lastname.getText())) {
            checklastName.setVisible(true);
            checklastName.setText("Last name must contain only characters [a-z], minimum length is 3");
            checklastName.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checklastName.setVisible(false);
        }

        if (tf_numtel.getText().isEmpty()) {
            CheckPhoneNumber.setVisible(true);
            CheckPhoneNumber.setText("Phone Number field cannot be blank.");
            CheckPhoneNumber.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckPhoneNumberConstraint(tf_numtel.getText())) {
            CheckPhoneNumber.setVisible(true);
            CheckPhoneNumber.setText("The telephone number must consist of exactly 8 digits.");
            CheckPhoneNumber.setStyle("-fx-text-fill: red;");
            verif = false;
        } /*else if (isAlreadyRegisteredWithPhoneNumber()) {
            CheckPhoneNumber.setVisible(true);
            CheckPhoneNumber.setText("Phone Number already exists");
            CheckPhoneNumber.setStyle("-fx-text-fill: red;");
            verif = false;
        }*/ else {
            CheckPhoneNumber.setVisible(false);
        }

        if (adress.getText().isEmpty()) {
            CheckAdresse.setVisible(true);
            CheckAdresse.setText("Address field cannot be blank.");
            CheckAdresse.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            CheckAdresse.setVisible(false);
        }
        if (tf_email.getText().isEmpty()) {
            CheckMail.setVisible(true);
            CheckMail.setText("Mail field cannot be blank.");
            CheckMail.setStyle("-fx-text-fill: red;");
            verif = false;
        } /*else if (isAlreadyRegisteredWithMail()) {
            CheckMail.setVisible(true);
            CheckMail.setText("Mail already exists");
            CheckMail.setStyle("-fx-text-fill: red;");
        }*/ else if (!isValidEmail(tf_email.getText())) {
            CheckMail.setVisible(true);
            CheckMail.setText("Invalid email format.");
            CheckMail.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            CheckMail.setVisible(false);
        }



        if (birthdate.getValue() == null) {
            checkDateBirthday.setVisible(true);
            checkDateBirthday.setText("Birthday Date field cannot be blank.");
            checkDateBirthday.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            checkDateBirthday.setVisible(false);
        }



        if (!verif) {
            return false;
        }
        return true;

    }

    /*
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

    }*/
    @FXML
    void update(ActionEvent event) {
        // Retrieve user details from text fields
        String userId = SessionManager.getInstance().getUserId();
        User user = userService.afficher(userId);

        if (checkIsValidated()) {
            String email = tf_email.getText();
            String firstname = tf_firstname.getText();
            String lastname = tf_lastname.getText();
            String numtel = tf_numtel.getText();
            String adresse = adress.getText();
            LocalDate birthdate = this.birthdate.getValue(); // Assuming MFXDatePicker returns LocalDate

            // Create a new User object with updated details
            User updatedUser = new User();
            updatedUser.setId(userId); // Set user ID
            updatedUser.setEmail(email);
            updatedUser.setNom(firstname);
            updatedUser.setPrenom(lastname);
            updatedUser.setNum_tel(numtel);
            updatedUser.setDate_N(birthdate);
            updatedUser.setAdress(adresse);
            updatedUser.setStatus(user.getStatus());
            updatedUser.setPwd(user.getPwd());
            updatedUser.setRole(user.getRole());
            updatedUser.setPhoto(photoPath);
            // Update user details in the database
            UserService userService = new UserService();
            boolean success = userService.updateUser(updatedUser);

            if (success) {
                // If update was successful, display success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("User details updated successfully!");
                alert.show();
            } else {
                // If update failed, display error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Failed to update user details. Please try again.");
                alert.show();
            }
            ;
        }
    }


    public void changepwd(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) update_btn.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/EnterPassword.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User SignIn");
        stage.show();
    }
}


