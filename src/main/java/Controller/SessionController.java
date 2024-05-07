package Controller;

import Entities.Session;
import Service.ServiceSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
//import javafx.scene.paint.Color;
import java.awt.Color;
import javafx.stage.Stage;
import Utils.MyDataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import static java.sql.Date.valueOf;

public class SessionController implements Initializable {
    private Stage primaryStage;
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;


    @FXML
    private TableView table;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnAjouter;
    @FXML
    private TextField tCap;
    @FXML
    private ComboBox<String> comboBoxType;

    @FXML
    private ComboBox<String> comboBoxCoach;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableColumn id;
    @FXML
    private TableColumn cap;
    @FXML
    private TableColumn type;
    @FXML
    private TableColumn date;
    @FXML
    private TableColumn coach;

    @FXML
    private TextField searchField;

    @FXML
    private Pagination pagination;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    private Label currentPageLabel;

    private ServiceSession serviceSession = new ServiceSession();
    private ObservableList<Session> sessions = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPageLabel = new Label();
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> handlePageChange());
        AfficherSE();

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Session session = (Session) table.getSelectionModel().getSelectedItem();

                if (session != null) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/ModifierSession.fxml"));
                        Parent root = fxmlLoader.load();

                        ModifierSession modifierSession = fxmlLoader.getController();

                        modifierSession.setIdA(String.valueOf(session.getId()));
                        modifierSession.setDatePicker(LocalDate.parse(session.getDate()));
                        modifierSession.setComboBoxType(session.getType());
                        modifierSession.setComboBoxCoach(session.getCoach());
                        modifierSession.settCap(String.valueOf(session.getCap()));

                        table.getScene().setRoot(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void AfficherSE() {
        try {
            List<Session> l = serviceSession.afficherList();
            sessions.clear();
            sessions.addAll(l);
            int totalPages = (int) Math.ceil(sessions.size() / 5.0);
            pagination.setPageCount(totalPages);
            pagination.setCurrentPageIndex(0);

            table.getItems().setAll(getCommandesForCurrentPage());

            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            cap.setCellValueFactory(new PropertyValueFactory<>("cap"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            coach.setCellValueFactory(new PropertyValueFactory<>("coach"));

            Map<String,List<Session>> sessionMap = sessions.stream().collect(Collectors.groupingBy(Session::getType));
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<String, List<Session>> entry : sessionMap.entrySet()) {
                String sessionType = entry.getKey();
                int totalCapacity = entry.getValue().stream().mapToInt(Session::getCap).sum();
                series.getData().add(new XYChart.Data<>(sessionType, totalCapacity));
            }

        lineChart.getData().clear();
        lineChart.getData().add(series);
            // Obtenir les objets CategoryAxis et NumberAxis pour les axes X et Y
            CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
            NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();

// Définir les titres des axes X et Y
            xAxis.setLabel("Sessions");
            yAxis.setLabel("Capacités");

        } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @FXML
    protected void handlePageChange() {
        table.getItems().setAll(getCommandesForCurrentPage());
        currentPageLabel.setText("Page " + (pagination.getCurrentPageIndex() + 1) + " / " + pagination.getPageCount());
    }

    private ObservableList<Session> getCommandesForCurrentPage() {
        int fromIndex = pagination.getCurrentPageIndex() * 5;
        int toIndex = Math.min(fromIndex + 5, sessions.size());
        List<Session> subList = sessions.subList(fromIndex, toIndex);
        return FXCollections.observableArrayList(subList);
    }

    @FXML
    void AJ(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/AjouterSession.fxml"));
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur");
        }
    }

    @FXML
    void deleteSession(ActionEvent event) {

        TableView.TableViewSelectionModel<Session> selectionModel = table.getSelectionModel();
        if (!selectionModel.isEmpty()) {
            ServiceSession es = new ServiceSession();
            Session selectedSession = selectionModel.getSelectedItem();
            try {
                es.delete(selectedSession);
                AfficherSE();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }

    private void showErrorAlert(String s) {
    }

    private void showSuccessMessage(String s) {
    }

    @FXML
    void refreshSessions() {
    }

    private void searchSessions(String keyword) {

        ObservableList<Session> allSessions = getSesions();
        ObservableList<Session> filteredSessions = FXCollections.observableArrayList();

        for (Session session : allSessions) {
            if (session.getType().toLowerCase().contains(keyword.toLowerCase())) {
                filteredSessions.add(session);
            }
        }
        table.setItems(filteredSessions);
    }
    public ObservableList<Session> getSesions() {
        ObservableList<Session> session = FXCollections.observableArrayList();
        String query = "select * from session";
        con = MyDataBase.getInstance().getConnection();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Session s = new Session();
                s.setId(rs.getInt("id"));
                s.setType(rs.getString("type"));
                s.setDate(rs.getString("date"));
                s.setCap(rs.getInt("cap"));
                session.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return session;
    }

    public void AfficherSESearch(KeyEvent keyEvent) {
        ServiceSession ss = new ServiceSession();
        try {
            List<Session> l = ss.afficherListSearch(searchField.getText());

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Session session : l) {
                table.getItems().add(session);
            }

            // Associer les propriétés des objets Exercice aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            cap.setCellValueFactory(new PropertyValueFactory<>("cap"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            coach.setCellValueFactory(new PropertyValueFactory<>("coach"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void PDF() throws IOException {
        ServiceSession ss = new ServiceSession();
        try {
            List<Session> listeSessions = ss.afficherList();

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())); // Créer une page en mode horizontal
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16); // Police en gras et taille augmentée pour le titre
                    contentStream.setNonStrokingColor(Color.RED); // Couleur rouge pour le titre

                    contentStream.beginText();
                    contentStream.newLineAtOffset(150, 750); // Décalage horizontal pour le titre
                    contentStream.showText("COMMANDES"); // Titre étendu
                    contentStream.endText();

                    float margin = 20;
                    float yStart = 580;
                    float rowHeight = 50;
                    float tableWidth = 800; // Largeur du tableau augmentée

                    // Dessiner les lignes horizontales du tableau
                    int rows = listeSessions.size();
                    for (int i = 0; i <= rows+1; i++) { // Change rows to rows + 1 to include the bottom line
                        contentStream.setStrokingColor(Color.BLACK);
                        contentStream.moveTo(margin, yStart - i * rowHeight);
                        contentStream.lineTo(margin + tableWidth, yStart - i * rowHeight);
                        contentStream.stroke();
                    }

                    String[] headers = {"ID", "cap", "type", "date", "session"};
                    float colWidth = tableWidth / headers.length;

                    // Dessiner les lignes verticales du tableau
                    // Dessiner les lignes verticales du tableau
                    for (int i = 0; i <= headers.length; i++) {
                        contentStream.moveTo(margin + i * colWidth, yStart);
                        contentStream.lineTo(margin + i * colWidth, yStart - (rows + 1) * rowHeight); // Adjust rows + 1
                        contentStream.stroke();
                    }


                    // Ajouter les en-têtes
                    for (int i = 0; i < headers.length; i++) {
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12); // Police en gras pour les en-têtes
                        contentStream.setNonStrokingColor(Color.RED); // Couleur bleue pour les en-têtes
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin + i * colWidth + 5, yStart - 15);
                        contentStream.showText(headers[i]);
                        contentStream.endText();
                    }

                    // Ajouter les données
                    for (int i = 0; i < listeSessions.size(); i++) {
                        Session ex = listeSessions.get(i);

                        String[] data = {
                                String.valueOf(ex.getId()),
                                String.valueOf(ex.getCap()),
                                String.valueOf(ex.getCoach()),
                                String.valueOf(ex.getDate()),
                                String.valueOf(ex.getType())
                        };

                        for (int j = 0; j < data.length; j++) {
                            float x = margin + j * colWidth + 5;
                            float y = yStart - (i + 1) * rowHeight - 15;
                            contentStream.setFont(PDType1Font.HELVETICA, 10); // Taille de police réduite pour les données
                            contentStream.setNonStrokingColor(Color.BLACK); // Couleur noire pour les données
                            contentStream.beginText();
                            String texteSansSautDeLigne = data[j].replace("\n", ""); // Supprimer les sauts de ligne
                            if (texteSansSautDeLigne.length() > 30) {
                                // Si la donnée dépasse 30 caractères, diviser le texte en deux lignes
                                contentStream.newLineAtOffset(x, y);
                                contentStream.showText(texteSansSautDeLigne.substring(0, 30));
                                contentStream.newLineAtOffset(0, -12); // Décalage vertical pour la deuxième ligne
                                contentStream.showText(texteSansSautDeLigne.substring(30));
                            } else {
                                contentStream.newLineAtOffset(x, y);
                                contentStream.showText(texteSansSautDeLigne);
                            }
                            contentStream.endText();
                        }
                    }

                    contentStream.close();
                    document.save("TableauSession.pdf");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }}















    public void Reservation(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Reservation-front.fxml"));
            primaryStage=(Stage)((Node)event.getSource()).getScene().getWindow();
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