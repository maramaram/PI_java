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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import Utils.MyDataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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

    int myIndex;
    @FXML
    private Pagination pagination;

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
                s.setCap(rs.getInt("cap"));
                s.setDate(String.valueOf(rs.getDate("date")));
                s.setCoach(rs.getString("coach"));
                session.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return session;
    }

    public void createReservation(ActionEvent actionEvent) {
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
}
