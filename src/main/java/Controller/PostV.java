package Controller;

import Entities.Exercice;
import Entities.Post;
import Entities.User;
import Service.PostService;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;



public class PostV {


    private Stage primaryStage;

    @FXML
    private VBox vbox;
    @FXML
    private VBox testbox;

    @FXML
    private TextField search ;

    private static final String API_URL = "https://api.chucknorris.io/jokes/random";

    @FXML
    public void initialize() {




        AfficherEX(); // Appeler la méthode pour afficher les données
        AfficherJoke();
        AfficherWeather();

    }


    @FXML
    private VBox adbox;
    @FXML
    protected void AfficherWeather() {
            String location = "Tunisia"; // Specify the location for weather information
        String apiKey = "";

        try {
            String url = "https://api.tomorrow.io/v4/timelines?location=" + location + "&fields=temperature&apikey=" + apiKey;

            // Create HTTP connection
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                // Parse JSON response
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray timelines = data.getJSONArray("timelines");
                JSONObject firstTimeline = timelines.getJSONObject(0);
                JSONArray intervals = firstTimeline.getJSONArray("intervals");
                JSONObject firstInterval = intervals.getJSONObject(0);
                JSONObject temperatureData = firstInterval.getJSONObject("values");
                double temperature = temperatureData.getDouble("temperature");

                // Create and style weather information display
                Label temperatureLabel = new Label("Current Temperature: " + temperature + "°C");
                temperatureLabel.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold; -fx-text-fill: #333333;");

                // Create ImageView based on temperature
                ImageView temperatureImageView = createTemperatureImageView(temperature);

                // Clear existing content in VBox
                adbox.getChildren().clear();

                // Add temperature label and ImageView to VBox
                adbox.getChildren().addAll(temperatureLabel, temperatureImageView);

                // Center the content in VBox
                adbox.setAlignment(javafx.geometry.Pos.CENTER);

                // Apply styles to the VBox
                adbox.setStyle(
                        "-fx-background-color: #f0f0f0; " + // Set background color
                                "-fx-border-color: #da5f46; " + // Set border color
                                "-fx-border-width: 2px; " + // Set border width
                                "-fx-border-radius: 5px; " + // Set border radius
                                "-fx-padding: 15px;" // Add padding
                );

            } else {
                System.out.println("Failed to retrieve weather data. HTTP error code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle network or API errors
        }
    }

    // Method to create ImageView based on temperature
    private ImageView createTemperatureImageView(double temperature) {
        // Determine the image path based on the temperature value
        String imagePath;
        if (temperature < 10) {
            imagePath = "/images/cold.png";
        } else if (temperature >= 10 && temperature < 30) {
            imagePath = "/images/cool.png";
        } else {
            imagePath = "/images/hot.png";
        }

        // Load the image and create ImageView
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100); // Set image height
        imageView.setFitWidth(100); // Set image width

        return imageView;
    }



    @FXML
    protected void AfficherJoke() {
        try {
            URL url = new URL("https://api.chucknorris.io/jokes/random");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = conn.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            String joke = jsonObject.getString("value");

            Label jokeLabel = new Label(joke);
            jokeLabel.setWrapText(true);
            jokeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;"); // Appliquer une couleur de texte personnalisée

            // Créer un bouton pour obtenir une nouvelle blague
            Button newJokeButton = new Button("Obtenir une autre blague");
            newJokeButton.setOnAction(event -> AfficherJoke());
            newJokeButton.setStyle(
                    "-fx-background-color: #da5f46; " + // Définir la couleur de fond
                            "-fx-text-fill: white; " + // Définir la couleur du texte
                            "-fx-font-size: 14px; " + // Définir la taille de la police
                            "-fx-pref-width: 200px; " + // Définir la largeur préférée
                            "-fx-padding: 8px; " + // Ajouter du padding
                            "-fx-border-radius: 5px; " + // Ajouter un rayon de bordure
                            "-fx-border-color: #da5f46;" // Définir la couleur de la bordure
            );

            // Effacer les données existantes de la VBox
            testbox.getChildren().clear();

            // Appliquer le CSS à la VBox
            testbox.setStyle(
                    "-fx-background-color: #f0f0f0; " + // Définir la couleur de fond
                            "-fx-border-color: #da5f46; " + // Définir la couleur de la bordure
                            "-fx-border-width: 2px; " + // Définir la largeur de la bordure
                            "-fx-border-radius: 5px; " + // Définir le rayon de la bordure
                            "-fx-padding: 15px;" // Ajouter du padding
            );

            // Ajouter le bouton en haut de la VBox
            VBox.setMargin(newJokeButton, new Insets(10, 0, 10, 0)); // Ajouter des marges au bouton
            testbox.getChildren().add(newJokeButton);

            // Ajouter l'étiquette du joke sous le bouton dans la VBox
            VBox.setMargin(jokeLabel, new Insets(10, 0, 0, 0)); // Ajouter des marges à l'étiquette du joke
            testbox.getChildren().add(jokeLabel);

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    protected void AfficherEX() {
        PostService es = new PostService();
        UserService userService = new UserService();
        try {
            List<Post> l = es.afficherList();

            // Clear existing data from the VBox
            vbox.getChildren().clear();

            // Iterate through the list of posts
            for (int i = 0; i < l.size(); i += 3) {

                // Create an HBox for each group of four posts
                HBox hbox = new HBox();
                hbox.setSpacing(20); // Add space between the elements in the HBox
                hbox.setAlignment(Pos.CENTER);

                // Add each post to the HBox
                for (int j = i; j < i + 3 && j < l.size(); j++) {
                    // Create a VBox for each post
                    VBox postBox = new VBox();
                    postBox.setAlignment(Pos.CENTER);
                    postBox.setSpacing(12);

                    // Style for the VBox (post container)
                    postBox.setStyle(
                            "-fx-background-color: #ffffff; " +
                                    "-fx-border-color: #da5f46; " +
                                    "-fx-border-width: 2px; " +
                                    "-fx-border-style: solid; " +
                                    "-fx-border-radius: 8px; " +
                                    "-fx-background-radius: 8px; " +
                                    "-fx-padding: 15px; " +
                                    "-fx-shadow-highlight-color: rgba(0,0,0,0.1); " +
                                    "-fx-shadow-highlight-radius: 5;"
                    );

                    // Adjust the minimum and maximum width of the VBox
                    postBox.setMinWidth(300);
                    postBox.setMaxWidth(300);

                    Post post = l.get(j);

                    // Add the post's elements to the VBox
                    ImageView postImage = post.getImage1();
                    postImage.setFitWidth(260);
                    postImage.setFitHeight(180);

                    // Style for the ImageView (post image)
                    postImage.setStyle(
                            "-fx-border-color: #da5f46; " +
                                    "-fx-border-width: 2px; " +
                                    "-fx-border-radius: 8px; " +
                                    "-fx-background-radius: 8px; " +
                                    "-fx-shadow-highlight-color: rgba(0,0,0,0.2); " +
                                    "-fx-shadow-highlight-radius: 5;"
                    );

                    postImage.setOnMouseClicked(event -> detail(post));

                    // Create labels for title, content, and date
                    Label titleLabel = new Label(post.getTitle());
                    titleLabel.setOnMouseClicked(event -> detail(post));

                    // Style for the title label
                    titleLabel.setStyle(
                            "-fx-alignment: center; " +
                                    "-fx-font-size: 22px; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-text-fill: #333;"
                    );

                    Label contentLabel = new Label(post.getContenu());

                    // Style for the content label
                    contentLabel.setStyle("-fx-text-fill: #666; -fx-padding: 5px;");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    // Create an HBox to contain the "vu" and "commentaire" icons along with the date
                    HBox iconDateBox = new HBox();
                    iconDateBox.setAlignment(Pos.CENTER_LEFT);
                    iconDateBox.setSpacing(5);

                    User user = userService.getUserById(post.getUser_id());
                    VBox userImageBox = new VBox();
                    userImageBox.setAlignment(Pos.CENTER);
                    userImageBox.setSpacing(2);

                    ImageView userPhoto = user.getImage1();
                    userPhoto.setFitWidth(45);
                    userPhoto.setFitHeight(45);
                    userImageBox.getChildren().add(userPhoto);

                    // Create HBox for user name
                    HBox userNameBox = new HBox();
                    userNameBox.setSpacing(2);



                    Label userNameLabel = new Label(" " +user.getNom() + " " + user.getPrenom());
                    userNameLabel.setStyle("-fx-font-weight: bold;");
                    userNameBox.getChildren().add(userNameLabel);

                    VBox tout = new VBox();
                    tout.getChildren().addAll(userNameLabel,iconDateBox);



                    HBox userdetail = new HBox();
                    userdetail.getChildren().addAll(userImageBox,tout);




                    Label vuLabel = new Label(String.valueOf(post.getViews()));
                    Label separatorLabel = new Label(" ");
                    Label separatorLabel2 = new Label("|");

                    // Add "vu" icon
                    ImageView vuIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/vu.png")));
                    vuIcon.setFitHeight(15);
                    vuIcon.setFitWidth(15);

                    // Add "commentaire" icon
                    ImageView commentaireIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/comment.png")));
                    commentaireIcon.setFitHeight(15);
                    commentaireIcon.setFitWidth(15);

                    // Add date label
                    Label dateLabel = new Label(dateFormat.format(post.getDate()));

                    Label nbcommentLabel = new Label(String.valueOf(es.countComments(post.getId())));

                    // Style for the date label
                    dateLabel.setStyle("-fx-text-fill: #999; -fx-padding: 5px;");

                    // Add icons and date label to the iconDateBox
                    iconDateBox.getChildren().addAll(dateLabel,separatorLabel,nbcommentLabel, commentaireIcon, separatorLabel2, vuLabel,vuIcon );

                    // Create modifier and supprimer buttons
                    Button modifierButton = new Button("Modifier");
                    Button supprimerButton = new Button("Supprimer");

                    // Set preferred widths for the buttons
                    modifierButton.setPrefWidth(130);
                    supprimerButton.setPrefWidth(130);

                    // Add event handlers to the buttons
                    modifierButton.setOnAction(event -> handleModifier(post));
                    supprimerButton.setOnAction(event -> handleSupprimer(post));

                    // Create an HBox to contain the modifier and supprimer buttons
                    HBox buttonBox = new HBox(modifierButton, supprimerButton);
                    buttonBox.setSpacing(10); // Add space between buttons if needed

                    // Add the elements to the VBox
                    postBox.getChildren().addAll(titleLabel, postImage, userdetail, buttonBox);

                    // Add the postBox to the HBox
                    hbox.getChildren().add(postBox);
                }

                // Add the HBox to the VBox
                VBox.setMargin(hbox, new Insets(15));
                vbox.getChildren().add(hbox);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    // Event handler for Modifier button
    private void handleModifier(Post post) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Post/PostModV.fxml"));
            Parent root = fxmlLoader.load();

            // Obtenir le contrôleur associé au fichier FXML chargé
            PostModV PostModV = fxmlLoader.getController();

            // Remplir les champs du contrôleur avec les données de l'post sélectionné
            PostModV.setIdA(String.valueOf(post.getId()));
            PostModV.setTitleA(post.getTitle());
            PostModV.setContenuA(post.getContenu());


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            PostModV.setDateA(dateFormat.format(post.getDate()));

            PostModV.setViewsA(String.valueOf(post.getViews()));
            PostModV.setUserA(String.valueOf(post.getViews()));
            PostModV.setImageA(post.getImage());



            primaryStage=(Stage)search.getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }

    // Event handler for Supprimer button
    private void handleSupprimer(Post post) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer ce post ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                PostService postService = new PostService();
                postService.delete(post); // Assuming delete method takes the post object as parameter
                AfficherEX(); // Refresh the display
            } catch (SQLException e) {
                System.out.println("Erreur lors de la suppression du post : " + e.getMessage());
            }
        }
    }




    @FXML
    protected void AfficherEXSearch() {
        PostService es = new PostService();
        try {
            List<Post> l = es.afficherListSearch(search.getText());
            // Clear existing data from the VBox
            vbox.getChildren().clear();


            // Iterate through the list of posts
            for (int i = 0; i < l.size(); i += 2) {
                // Create an HBox for each group of four posts
                HBox hbox = new HBox();
                hbox.setSpacing(20); // Add space between the elements in the HBox
                hbox.setAlignment(Pos.CENTER);

                // Add each post to the HBox
                for (int j = i; j < i + 2 && j < l.size(); j++) {
                    // Create a VBox for each post
                    VBox postBox = new VBox();
                    postBox.setSpacing(12);

                    // Style for the VBox (post container)
                    postBox.setStyle(
                            "-fx-background-color: #ffffff; " +
                                    "-fx-border-color: #da5f46; " +
                                    "-fx-border-width: 2px; " +
                                    "-fx-border-style: solid; " +
                                    "-fx-border-radius: 8px; " +
                                    "-fx-background-radius: 8px; " +
                                    "-fx-padding: 15px; " +
                                    "-fx-shadow-highlight-color: rgba(0,0,0,0.1); " +
                                    "-fx-shadow-highlight-radius: 5;"
                    );

                    // Adjust the minimum and maximum width of the VBox
                    postBox.setMinWidth(300);
                    postBox.setMaxWidth(300);

                    Post post = l.get(j);

                    // Add the post's elements to the VBox
                    ImageView postImage = post.getImage1();
                    postImage.setFitWidth(260);
                    postImage.setFitHeight(180);

                    // Style for the ImageView (post image)
                    postImage.setStyle(
                            "-fx-border-color: #da5f46; " +
                                    "-fx-border-width: 2px; " +
                                    "-fx-border-radius: 8px; " +
                                    "-fx-background-radius: 8px; " +
                                    "-fx-shadow-highlight-color: rgba(0,0,0,0.2); " +
                                    "-fx-shadow-highlight-radius: 5;"
                    );

                    postImage.setOnMouseClicked(event -> detail(post));

                    // Create labels for title, content, and date
                    Label titleLabel = new Label(post.getTitle());
                    titleLabel.setOnMouseClicked(event -> detail(post));

                    // Style for the title label
                    titleLabel.setStyle(
                            "-fx-alignment: center; " +
                                    "-fx-font-size: 22px; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-text-fill: #333;"
                    );

                    Label contentLabel = new Label(post.getContenu());

                    // Style for the content label
                    contentLabel.setStyle("-fx-text-fill: #666; -fx-padding: 5px;");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    // Create an HBox to contain the "vu" and "commentaire" icons along with the date
                    HBox iconDateBox = new HBox();
                    iconDateBox.setAlignment(Pos.CENTER_LEFT);
                    iconDateBox.setSpacing(5);

                    Label vuLabel = new Label(String.valueOf(post.getViews()));
                    Label separatorLabel = new Label(" ");
                    Label separatorLabel2 = new Label("|");

                    // Add "vu" icon
                    ImageView vuIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/vu.png")));
                    vuIcon.setFitHeight(15);
                    vuIcon.setFitWidth(15);

                    // Add "commentaire" icon
                    ImageView commentaireIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/comment.png")));
                    commentaireIcon.setFitHeight(15);
                    commentaireIcon.setFitWidth(15);

                    // Add date label
                    Label dateLabel = new Label(dateFormat.format(post.getDate()));

                    Label nbcommentLabel = new Label(String.valueOf(es.countComments(post.getId())));

                    // Style for the date label
                    dateLabel.setStyle("-fx-text-fill: #999; -fx-padding: 5px;");

                    // Add icons and date label to the iconDateBox
                    iconDateBox.getChildren().addAll(dateLabel,separatorLabel,nbcommentLabel, commentaireIcon, separatorLabel2, vuLabel,vuIcon );

                    // Create modifier and supprimer buttons
                    Button modifierButton = new Button("Modifier");
                    Button supprimerButton = new Button("Supprimer");

                    // Set preferred widths for the buttons
                    modifierButton.setPrefWidth(130);
                    supprimerButton.setPrefWidth(130);

                    // Add event handlers to the buttons
                    modifierButton.setOnAction(event -> handleModifier(post));
                    supprimerButton.setOnAction(event -> handleSupprimer(post));

                    // Create an HBox to contain the modifier and supprimer buttons
                    HBox buttonBox = new HBox(modifierButton, supprimerButton);
                    buttonBox.setSpacing(10); // Add space between buttons if needed

                    // Add the elements to the VBox
                    postBox.getChildren().addAll(titleLabel, iconDateBox, postImage, contentLabel, buttonBox);

                    // Add the postBox to the HBox
                    hbox.getChildren().add(postBox);
                }

                // Add the HBox to the VBox
                VBox.setMargin(hbox, new Insets(15));
                vbox.getChildren().add(hbox);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    public void Defis( ) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/DefiV.fxml"));
            primaryStage=(Stage)vbox.getScene().getWindow();
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

            Parent root = FXMLLoader.load(getClass().getResource("/Post/PostV.fxml"));
            primaryStage=(Stage)vbox.getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }


    }


    private void detail(Post ex) {
        try {
            // Charger le fichier FXML de la nouvelle scène
            // Charger le fichier FXML ExerciceDetails.fxml

            PostService es = new PostService();
            es.incrementViews(ex);


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Post/PostDetails.fxml"));
            Parent root = fxmlLoader.load();

            // Obtenir le contrôleur associé au fichier FXML chargé
            PostDetails postDetails = fxmlLoader.getController();

            // Remplir les champs du contrôleur avec les données de l'exercice sélectionné
            postDetails.setPost_id(ex.getId());
            postDetails.setTitleM(ex.getTitle());
            postDetails.setContenuM(ex.getContenu());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            postDetails.setDateM(dateFormat.format(ex.getDate()));
            postDetails.setIImageM(ex.getImage());
            vbox.getScene().setRoot(root);

        } catch (IOException | SQLException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }


    public void PostAddV( ) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Post/PostAddV.fxml"));
            primaryStage=(Stage)search.getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }


    public void Blogs( ) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Post/PostV.fxml"));
            primaryStage=(Stage)vbox.getScene().getWindow();
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

