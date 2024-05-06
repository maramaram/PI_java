package Controller;

public class basebackController {
    @FXML
    private Label logout_btn;
    public void Exercices(ActionEvent event) {
        loadFXML("/Exercice/Exercice-front.fxml", event);
    }
    public void Defis(ActionEvent event) {
        loadFXML("/Defi/Defi-front.fxml", event);
    }
    public void Commande(ActionEvent event) {
        loadFXML("/Commande/CommandeBack.fxml", event);
    }
    public void Livreur(ActionEvent event) {
        loadFXML("/Livreur/LivreurBack.fxml", event);
    }
    public void Blogs(ActionEvent event) {
        loadFXML("/Post/Post-front.fxml", event);
    }
    public void Comment(ActionEvent event) {
        loadFXML("/Comment/Comment-front.fxml", event);
    }
    private void loadFXML(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }

    public void logout(MouseEvent mouseEvent) {
        SessionManager.getInstance().cleanUserSessionAdmin();
        try {
            Node sourceNode = (Node) logout_btn;
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.close();
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/User/LogIn.fxml")));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
