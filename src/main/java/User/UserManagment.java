package User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Utils.DataBase;

import java.io.IOException;

public class UserManagment extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DataBase.class.getResource("/User/LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Breathe Out");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

}