package Controller;
import Entities.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Entities.*;
import Service.*;
import Utils.DataBase;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ForgotPwdController implements Initializable {


    @FXML
    private Label CheckPhoneNumber;

    @FXML
    private Label checkMailLogin;

    @FXML
    private ImageView copy;

    @FXML
    private ImageView go_back;

    @FXML
    private ImageView imageView;

    @FXML
    private Button sendCode_btn;

    @FXML
    private TextField phone_number;
    private final Connection con;

    private boolean isAlreadyRegisteredWithPhoneNumber() {
        PreparedStatement ps;
        ResultSet rs;
        boolean PhoneExist = false;
        String query = "select * from user WHERE num_tel = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, phone_number.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                PhoneExist = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return PhoneExist;
    }

    public ForgotPwdController(Connection con, Connection con1) {
        this.con = con1;
        DataBase dataBase = new DataBase();
        con = dataBase.getConnect();
    }

    @FXML
    void sendCode(ActionEvent event) {

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
        try {
            // Convert the file path to a URL
            File file = new File("C:/Users/bouaz/PREVIOUS/src/main/java/image/image-removebg-preview (3).png");
            String imageUrl = file.toURI().toURL().toString();
            // Create an Image object from the URL
            Image image = new Image(imageUrl);
            // Set the image to the ImageView
            go_back.setImage(image);
        } catch (MalformedURLException e) {
            // Handle invalid URL exception
            e.printStackTrace();
            // Optionally, show an alert or fallback image
        }
    }
}
