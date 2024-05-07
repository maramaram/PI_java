package Controller;


import Entities.Comment;
import Service.CommentService;
import Entities.User;
import Service.UserService;
import Service.ExerciceService;
import Service.PostService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class PostDetails {


    private Stage primaryStage;


    @FXML
    private VBox VboxC;
    @FXML
    private Label titleM;
    @FXML
    private Label contenuM;
    @FXML
    private Label dateM;
    @FXML
    private ImageView imageM;


    @FXML
    private TextField comm;

    @FXML
    private Button report;


    private int post_Id; // Variable pour stocker l'ID du post

    public void setPost_id(int selectedPostId) {

        this.post_Id = selectedPostId;
        AfficherEX();

    }


    @FXML
    public void initialize() {

        AfficherEX(); // Appeler la méthode pour afficher les données

    }




    protected void AfficherEX() {
        CommentService commentService = new CommentService();
        UserService userService = new UserService();

        try {
            List<Comment> comments = commentService.afficherListByPostId(post_Id);

            // Clear existing data from the VBox
            VboxC.getChildren().clear();

            for (Comment comment : comments) {
                User user = userService.getUserById(comment.getUser_id());

                if (user != null) {
                    // Create main HBox for each comment
                    HBox commentBox = new HBox();
                    commentBox.setSpacing(10); // Spacing between user image and user details/content
                    commentBox.setPadding(new Insets(10));
                    commentBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 5px; -fx-background-radius: 5px;");

                    // Create VBox for user image
                    VBox userImageBox = new VBox();
                    userImageBox.setAlignment(Pos.CENTER);
                    userImageBox.setSpacing(2);

                    ImageView userPhoto = user.getImage1(); // Assuming getPhoto() returns an Image object
                    userPhoto.setFitWidth(60);
                    userPhoto.setFitHeight(60);
                    userImageBox.getChildren().add(userPhoto);

                    // Create VBox for user details and comment content
                    VBox userDetailsAndCommentBox = new VBox();
                    userDetailsAndCommentBox.setSpacing(2);

                    // Create HBox for user name
                    HBox userNameBox = new HBox();
                    userNameBox.setSpacing(2);

                    Label userNameLabel = new Label(user.getNom() + " " + user.getPrenom());
                    userNameLabel.setStyle("-fx-font-weight: bold;");

                    userNameBox.getChildren().add(userNameLabel);

                    // Create VBox for comment content, likes, and actions
                    VBox commentContentBox = new VBox();
                    commentContentBox.setSpacing(2);

                    Label contenuLabel = new Label(comment.getContenu());
                    contenuLabel.setWrapText(true);
                    contenuLabel.setStyle(" -fx-text-fill: black;");

                    // Create HBox for likes and actions
                    HBox likesAndActionsBox = new HBox();
                    likesAndActionsBox.setSpacing(2); // Adjust spacing between likes and buttons

                    Label likesLabel = new Label("" + comment.getNblikes());
                    likesLabel.setStyle("-fx-font-size: 13px;");
                    likesLabel.setPadding(new Insets(7, 0, 0, 0));

                    Button likeButton = createIconButton("/images/like.png");
                    Button editButton = createIconButton("/images/modifier.png");
                    Button deleteButton = createIconButton("/images/delete.png");

                    likeButton.setOnAction(event -> handleLikeIconClick(comment));
                    editButton.setOnAction(event -> handleEditIconClick(comment));
                    deleteButton.setOnAction(event -> handleDeleteIconClick(comment));

                    likesAndActionsBox.getChildren().addAll(likesLabel, likeButton, editButton, deleteButton);

                    // Add content and likes/actions to commentContentBox
                    commentContentBox.getChildren().addAll(contenuLabel, likesAndActionsBox);

                    // Add user details (name) and comment content to userDetailsAndCommentBox
                    userDetailsAndCommentBox.getChildren().addAll(userNameBox, commentContentBox);

                    // Add user image and userDetailsAndCommentBox to commentBox
                    commentBox.getChildren().addAll(userImageBox, userDetailsAndCommentBox);

                    // Add the commentBox to the main VBox
                    VboxC.getChildren().add(commentBox);

                    // Add a separator between comments
                    Separator separator = new Separator(Orientation.HORIZONTAL);
                    separator.setStyle("-fx-opacity: 0.5;");
                    VboxC.getChildren().add(separator);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des commentaires : " + e.getMessage());
        }
    }

    // Utility method to create buttons with icons
    private Button createIconButton(String imagePath) {
        Button button = new Button();
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(17); // Set width of the image
        imageView.setFitHeight(17); // Set height of the image
        button.setGraphic(imageView); // Set the image view as graphic for the button
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"); // Remove background and border
        return button;
    }

    private void handleLikeIconClick(Comment comment) {
        try {
            CommentService commentService = new CommentService();
            commentService.incrementLikes(comment); // Méthode pour incrémenter le nombre de likes du commentaire
            AfficherEX(); // Rafraîchir l'affichage des commentaires
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du like au commentaire : " + e.getMessage());
        }
    }


    private void handleDeleteIconClick(Comment comment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer ce commentaire ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                CommentService es = new CommentService();
                es.delete(comment);
                AfficherEX();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleEditIconClick(Comment comment) {
        // Create a custom dialog for editing the comment content
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Modifier le commentaire");

        // Set the header text
        dialog.setHeaderText(null);

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/images/modifier.png")));
        icon.setFitWidth(50); // Définissez la largeur souhaitée
        icon.setFitHeight(50); // Définissez la hauteur souhaitée

// Utilisez l'objet ImageView pour définir l'icône du dialog
        dialog.setGraphic(icon);

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create and configure the content text field
        TextArea textArea = new TextArea(comment.getContenu());
        textArea.setPromptText("Nouveau contenu du commentaire");
        textArea.setPrefRowCount(4);
        textArea.setWrapText(true);
        dialog.getDialogPane().setContent(textArea);

        // Request focus on the text area by default
        Platform.runLater(textArea::requestFocus);

        // Convert the result to a string when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return textArea.getText();
            }
            return null;
        });

        // Show the dialog and handle the result
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newContent -> {
            // Validate the new content before updating the comment
            if (!newContent.trim().isEmpty()) {
                try {
                    CommentService commentService = new CommentService();
                    comment.setContenu(newContent);
                    commentService.modifier(comment);
                    AfficherEX();
                } catch (SQLException e) {
                    System.out.println("Erreur lors de la modification du commentaire : " + e.getMessage());
                }
            } else {
                // Show an error message if the content is empty
                showErrorAlert("Le contenu du commentaire ne peut pas être vide.");
            }
        });
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void setTitleM(String titleM) {
        this.titleM.setText(titleM);
        this.titleM.setStyle("-fx-alignment: center; -fx-font-size: 30; -fx-font-weight: bold;");
    }

    public void setContenuM(String contenuM) {
        this.contenuM.setText(contenuM);
        this.contenuM.setWrapText(true);
    }

    public void setDateM(String dateM) {
        this.dateM.setText(dateM);
        this.dateM.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    }


    public void setIImageM(String imageM) {
        this.imageM.setImage(new Image(this.getClass().getResourceAsStream("/"+imageM)));
        this.imageM.setStyle("-fx-alignment: center;");

    }






    @FXML
    private void handleReportButtonAction(ActionEvent event) {
        // Create a dialog for user input
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Report Reason");
        dialog.setHeaderText("Please select reasons or provide details for the report:");

        // Add choice options to the dialog
        CheckBox racismCheckBox = new CheckBox("Racism");
        CheckBox sexismCheckBox = new CheckBox("Sexism");
        CheckBox violenceCheckBox = new CheckBox("Violence");
        CheckBox offensiveLanguageCheckBox = new CheckBox("Offensive Language");
        CheckBox inappropriateContentCheckBox = new CheckBox("Inappropriate Content");
        CheckBox harassmentCheckBox = new CheckBox("Harassment");
        CheckBox otherCheckBox = new CheckBox("Other");

        TextArea customReasonTextArea = new TextArea();
        customReasonTextArea.setPromptText("Type your custom reason here...");
        customReasonTextArea.setMinHeight(100); // Set a minimum height for the text area
        customReasonTextArea.setVisible(false); // Initially hide the custom reason text area

        // Inline CSS for styling
        String dialogStyle = "-fx-background-color: #f8f8f8;";
        String labelStyle = "-fx-font-weight: bold; -fx-font-size: 14px;";
        String textAreaStyle = "-fx-pref-width: 300px; -fx-pref-height: 100px;";

        // Apply styles to the dialog pane and components
        dialog.getDialogPane().setStyle(dialogStyle);

        // Add listener to otherCheckBox to show/hide customReasonTextArea
        otherCheckBox.setOnAction(e -> {
            customReasonTextArea.setVisible(otherCheckBox.isSelected());
            if (otherCheckBox.isSelected()) {
                customReasonTextArea.requestFocus(); // Set focus to the text area when "Other" is selected
            }
        });

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(
                racismCheckBox, sexismCheckBox, violenceCheckBox,
                offensiveLanguageCheckBox, inappropriateContentCheckBox,
                harassmentCheckBox, otherCheckBox, customReasonTextArea
        );
        dialog.getDialogPane().setContent(dialogContent);

        // Add styles to individual components
        racismCheckBox.setStyle(labelStyle);
        sexismCheckBox.setStyle(labelStyle);
        violenceCheckBox.setStyle(labelStyle);
        offensiveLanguageCheckBox.setStyle(labelStyle);
        inappropriateContentCheckBox.setStyle(labelStyle);
        harassmentCheckBox.setStyle(labelStyle);

        customReasonTextArea.setStyle(textAreaStyle);

        // Add buttons to the dialog pane
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        // Show the dialog and wait for user input
        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeOk) {
                // Check which options were selected
                StringBuilder reasons = new StringBuilder();
                if (racismCheckBox.isSelected()) {
                    reasons.append("Racism, ");
                }
                if (sexismCheckBox.isSelected()) {
                    reasons.append("Sexism, ");
                }
                if (violenceCheckBox.isSelected()) {
                    reasons.append("Violence, ");
                }
                if (offensiveLanguageCheckBox.isSelected()) {
                    reasons.append("Offensive Language, ");
                }
                if (inappropriateContentCheckBox.isSelected()) {
                    reasons.append("Inappropriate Content, ");
                }
                if (harassmentCheckBox.isSelected()) {
                    reasons.append("Harassment, ");
                }
                if (otherCheckBox.isSelected()) {
                    String customReason = customReasonTextArea.getText().trim();
                    if (!customReason.isEmpty()) {
                        reasons.append(customReason);
                    }
                }

                if (reasons.length() > 0) {
                    // Remove trailing comma and space
                    String selectedReasons = reasons.toString().replaceAll(", $", "");
                    sendEmailToAdmin(selectedReasons);
                } else {
                    System.out.println("Please select at least one reason.");
                }
            }
        });
    }

    private void sendEmailToAdmin(String reason) {
        String subject = "Publication signalée"; // Email subject
        String senderEmail = "yahya.benaicha@esprit.tn";
        String senderPassword = "uqfryseqdngsnxph";
        String adminEmail = "yahya.benaicha@esprit.tn";
        String smtpHost = "smtp.gmail.com";
        int smtpPort = 587;

        // Configure SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost); // SMTP server address
        properties.put("mail.smtp.port", smtpPort); // SMTP port

        // Create a Session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail));
            message.setSubject(subject);
            message.setText("La publication de ID("+ post_Id +") par un utilsateur. \n\nRaison du signalement:\n" + reason);

            // Send the email
            Transport.send(message);

            System.out.println("Email envoyé avec succès à l'administrateur !");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Échec de l'envoi de l'e-mail. Veuillez réessayer.");
        }
    }













    public void Defis( ) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/DefiV.fxml"));
            primaryStage=(Stage)contenuM.getScene().getWindow();
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
            primaryStage=(Stage)contenuM.getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }



    @FXML
    protected void AjouterEX() {
        CommentService commentService = new CommentService();
        boolean isValid = true;

        // Store the comment text in a local variable
        String commentText = comm.getText().trim();

        // Check if the comment text is empty
        if (commentText.isEmpty()) {
            isValid = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Champs vides");
            alert.setHeaderText("Veuillez remplir le champ de commentaire.");
            alert.setContentText("Le champ de commentaire est vide. Veuillez entrer un commentaire avant de continuer.");
            alert.showAndWait();
        }

        // Proceed with adding the comment if the text is valid
        if (isValid) {
            int postId = post_Id; // Assuming post_Id is set correctly elsewhere
            Comment comment = new Comment(commentText, postId,1);
            try {
                commentService.add(comment);
                // Clear the text field after successfully adding the comment
                comm.setText("");

                // Refresh the comments section
                AfficherEX();
            } catch (SQLException e) {
                // Display error alert with the SQLException message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Échec de l'ajout du commentaire");
                alert.setContentText("Une erreur s'est produite lors de l'ajout du commentaire : " + e.getMessage());
                alert.showAndWait();
            }
        }
    }



    @FXML
    public void Blogs(MouseEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène
            Parent root = FXMLLoader.load(getClass().getResource("/Post/PostV.fxml"));

            // Obtenir la scène à partir de l'événement source
            primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la nouvelle scène à la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }



}

