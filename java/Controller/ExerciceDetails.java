package Controller;

import Entities.Exercice;
import Service.ExerciceService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;


import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ExerciceDetails {


    private Stage primaryStage;


    @FXML
    private Label nomM;
    @FXML
    private Label desM;
    @FXML
    private Label mcM;
    @FXML
    private ImageView ndM;
    @FXML
    private ImageView image;
    @FXML
    private ImageView chat;
    @FXML
    private TextArea fo9;
    @FXML
    private VBox vchat;

    private static final String API_KEY = System.getenv("API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    public void setNomM(String nomM) {
        this.nomM.setText(nomM);
        this.nomM.setStyle("-fx-alignment: center; -fx-font-size: 30; -fx-font-weight: bold;");
    }

    public void setDesM(String desM) {
        this.desM.setText(desM);
        this.desM.setWrapText(true);
        this.desM.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: #f8f8f8; -fx-padding: 10px;");

    }

    public void setMcM(String mcM) {
        this.mcM.setText(mcM);
        this.mcM.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #000033;");


    }

    public void setNdM(String ndM) {
        if (ndM != null) {
            if (ndM.equals("1")) {
                this.ndM.setImage(new Image(this.getClass().getResourceAsStream("/Front/images/exo/dif1.png")));
            } else if (ndM.equals("2")) {
                this.ndM.setImage(new Image(this.getClass().getResourceAsStream("/Front/images/exo/dif2.png")));
            } else {
                this.ndM.setImage(new Image(this.getClass().getResourceAsStream("/Front/images/exo/dif3.png")));
            }
        } else {
            // Gérer le cas où ndM est null
            // Par exemple, vous pouvez définir une image par défaut
            this.ndM.setImage(new Image(this.getClass().getResourceAsStream("/Front/images/exo/default.png")));
        }
        this.ndM.setFitWidth(100);
        this.ndM.setFitHeight(75);
    }

    public void setIiimage(String imgM) {
        this.image.setImage(new Image(this.getClass().getResourceAsStream("/"+imgM)));
        this.image.setStyle("-fx-alignment: center;");


    }




    @FXML
    public void initialize() {
        // Load the CSS file
        // Set the chat container style
        vchat.getStyleClass().add("chat-container");

        // Set the VBox properties
        vchat.setManaged(false);
        vchat.prefHeightProperty().bind(Bindings.size(vchat.getChildren()).multiply(50).add(10));

        // Add the event handler for the send button
        chat.setOnMouseClicked(event -> {
            String message = fo9.getText().trim();
            if (!message.isEmpty()) {
                try {
                    String response = sendMessage(message);

                    // Create the message bubbles
                    MessageBubble userBubble = new MessageBubble(message, true);
                    MessageBubble assistantBubble = new MessageBubble(response, false);

                    // Add the message bubbles to the chat container
                    vchat.getChildren().addAll(userBubble, assistantBubble);

                    // Scroll to the bottom of the chat container


                } catch (IOException e) {
                    e.printStackTrace();
                }
                fo9.clear(); // Clear the text field after sending the message
            }
        });
    }


    public static String sendMessage(String message) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo-instruct");
        requestBody.addProperty("prompt", message);
        requestBody.addProperty("max_tokens", 300);

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .post(RequestBody.create(JSON, gson.toJson(requestBody)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            JsonObject responseBody = gson.fromJson(response.body().string(), JsonObject.class);
            return responseBody.getAsJsonArray("choices").get(0).getAsJsonObject().get("text").getAsString();
        }
    }

    public void Defis( ) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/DefiV.fxml"));
            primaryStage=(Stage)desM.getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }

    public void Exercices() {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/ExerciceV.fxml"));
            primaryStage=(Stage)desM.getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }

}

