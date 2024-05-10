package Controller;

import Entities.SessionManager;
import com.example.projectpi.HelloApplication;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Openai  {
    String userId = SessionManager.getInstance().getUserId();

    private static final String API_URL = "https://api.openai.com/v1/engines/text-davinci-003/completions";
    private static final String API_KEY= "Ysk-proj-WRl5dH7v2aFc1QdNL5y6T3BlbkFJDifon9rPGpCnUGMSV0ig";
  //

    @FXML
    private Label answerLabel;

    @FXML
    private TextArea questionTextArea;
    @FXML
    private Label cart;
    @FXML
    private Label shop;

    @FXML
    void viewcart(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paniers/basefrontp.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewshop(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/products/basefront.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void askQuestion(ActionEvent event) {
        // Get the question from the text area
        String question = questionTextArea.getText();

        // Call the method from the OpenaiService class to get the answer
        String answer = OpenaiService.index11(question);

        // Set the answer in the answerLabel
        answerLabel.setText(answer);
    }

    public static class OpenaiService {

        public static String index11(String question) {
            if (question == null || question.isEmpty()) {
                return "I'm sorry, I can only answer questions related to  products and Cart.";
            }

            try {
                String answer;
                if (question.matches("(?i).*conditions|termes|utilisation|propriété.*")) {
                    answer = getTermsAndConditions();
                } else if (question.matches("(?i).*shop|site|buy|credibility.*")) {
                    answer = " Breath_Out is a website to help you buy,sports products easily and safely.";
                } else if (question.matches("(?i).*|how to confirm my order|How can I validate this?|confirmation.*")) {
                    answer = "Once your order has been placed on Breath_Out, you will receive an SMS confirmation to ensure that your purchase is successfully completed..";
                } else if (question.matches("(?i).*payment|delivery|online.*")) {
                    answer = "With Breath_Out, you can pay online with peace of mind and offers a fast and reliable delivery service to get your favorite sporting goods right to your door.";
                } else if (question.matches("(?i).*blog|comment|like|want.*")) {
                    answer = "you can add a blog or comment on one leave a like and dont forget to share it with your friends";
                } else if (question.matches("(?i).*don|donnation|give|charity|want.*")) {
                    answer = "you can donate money easily from our site its very easy and secure";
                } else {
                    answer = getAnswerFromAPI(question);
                }
                return answer;
            } catch (Exception e) {
                e.printStackTrace();
                return "I'm sorry, I can only answer questions related to Breath-Out products and Cart. Please try again.";
            }
        }

        private static String getTermsAndConditions() {
            return "En utilisant l'application JardinDars, vous acceptez les présentes conditions d'utilisation...";
        }
        private static String getAnswerFromAPI(String question) throws Exception {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setDoOutput(true);

            String postData = "{"
                    + "\"prompt\": \"" + question + "\","
                    + "\"temperature\": 0.7,"
                    + "\"max_tokens\": 4000,"
                    + "\"top_p\": 1,"
                    + "\"frequency_penalty\": 0.5,"
                    + "\"presence_penalty\": 0"
                    + "}";

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(postData);
                wr.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    String jsonResponse = response.toString();
                    // Use regex to extract email address from the answer if present
                    Pattern pattern = Pattern.compile("(?<=contact@jardindart.com).*?$", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(jsonResponse);
                    if (matcher.find()) {
                        return matcher.group().trim();
                    }
                    return jsonResponse;
                }
            } else {
                // Handle different HTTP response codes
                if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    return "Bad request: Please check your input and try again.";
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    return "Unauthorized: Please check your API key.";
                } else {
                    return "Failed to retrieve response from API. Response code: " + responseCode;
                }
            }
        }



    }
}
