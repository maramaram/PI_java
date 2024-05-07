package Controller;

import Entities.Reservation;
import Entities.Session;
import Service.ServiceReservation;
import Service.ServiceSession;
import Utils.MyDataBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReservationController implements Initializable {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btnDelete;


    @FXML
    private Button btnAjouter;

    @FXML
    private Pagination pagination;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> comboBoxClient;
    @FXML
    private ComboBox<String> comboBoxSession;

    @FXML
    private TextField tEtat;

    @FXML
    private TableColumn<Reservation, Integer> id;

    @FXML
    private TableColumn<Reservation, String> session;

    @FXML
    private TableColumn<Reservation, LocalDate> date;

    @FXML
    private TableColumn<Reservation, String> etat;
    @FXML
    private TableColumn<Reservation, String> client;

    @FXML
    private TableView<Reservation> table;

    @FXML
    private TextField searchField;
    private ServiceReservation serviceReservation = new ServiceReservation();
    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    @FXML
    private Label currentPageLabel;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPageLabel = new Label();
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> handlePageChange());
        AfficherRE();

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Reservation reservation = (Reservation) table.getSelectionModel().getSelectedItem();

                if (reservation != null) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/ModifierReservation.fxml"));
                        Parent root = fxmlLoader.load();

                        ModifierReservation modifierReservation = fxmlLoader.getController();
                        modifierReservation.setIdA(String.valueOf(reservation.getId()));
                        modifierReservation.setDatePicker(LocalDate.parse(reservation.getDate()));
                        modifierReservation.setComboBoxClient(String.valueOf(reservation.getClient()));
                        modifierReservation.setComboBoxSession(String.valueOf(reservation.getSession()));
                        modifierReservation.setComboBoxEtat(String.valueOf(reservation.getEtat()));

                        table.getScene().setRoot(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public ObservableList<Reservation> getReservations() {
        ObservableList<Reservation> reservation = FXCollections.observableArrayList();
        String query = "select * from reservation";
        con = MyDataBase.getInstance().getConnection();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {

                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setEtat(rs.getString("etat"));
                r.setClient(rs.getString("client"));  // Ajoutez cette ligne pour récupérer le nom du client
                r.setSession(rs.getString("session"));
                r.setDate(rs.getString("date"));
                reservation.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservation;
    }


    public void AfficherRE() {
        try {

            List<Reservation> l = serviceReservation.afficherList();
            reservations.clear();
            reservations.addAll(l);
            int totalPages = (int) Math.ceil(reservations.size() / 5.0);
            pagination.setPageCount(totalPages);
            pagination.setCurrentPageIndex(0);

            table.getItems().setAll(getCommandesForCurrentPage());

            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            client.setCellValueFactory(new PropertyValueFactory<>("client"));

            etat.setCellFactory(col -> new TableCell<Reservation, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.equals("1") ? "En cours" : "Terminée");
                    }
                }
            });

            client.setCellFactory(col -> new TableCell<Reservation, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            });

            session.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> param) {
                    Reservation reservation = param.getValue();
                    String sessionValue = reservation.getSession();

                    String sessionName = "";
                    switch (sessionValue) {
                        case "1":
                            sessionName = "hit";
                            break;
                        case "2":
                            sessionName = "endurance";
                            break;
                        case "3":
                            sessionName = "cross fit";
                            break;
                        case "4":
                            sessionName = "pilate";
                            break;
                        default:
                            sessionName = "muscu training";
                            break;
                    }
                    return new SimpleStringProperty(sessionName);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void handlePageChange() {
        table.getItems().setAll(getCommandesForCurrentPage());
        currentPageLabel.setText("Page " + (pagination.getCurrentPageIndex() + 1) + " / " + pagination.getPageCount());
    }

    private ObservableList<Reservation> getCommandesForCurrentPage() {
        int fromIndex = pagination.getCurrentPageIndex() * 5;
        int toIndex = Math.min(fromIndex + 5, reservations.size());
        List<Reservation> subList = reservations.subList(fromIndex, toIndex);
        return FXCollections.observableArrayList(subList);
    }

    @FXML
    void AJ(ActionEvent event) {
        // Code pour afficher la vue de création de réservation

        try {
            Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/AjouterReservation.fxml"));
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur");
        }
    }

    @FXML
    void deleteReservation(ActionEvent event) {

        TableView.TableViewSelectionModel<Reservation> selectionModel = table.getSelectionModel();
        if (!selectionModel.isEmpty()) {
            ServiceReservation rs = new ServiceReservation();
            Reservation selectedReservation = selectionModel.getSelectedItem();
            try {
                rs.delete(selectedReservation);
                AfficherRE();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }

    @FXML
    void sauvegarderReservation(ActionEvent event) {
        // Code pour sauvegarder une nouvelle réservation
    }

    @FXML
    void refreshReservations() {
        // Code pour rafraîchir la liste des réservations
    }

    private void searchReservations(String keyword) {
        ObservableList<Reservation> allReservation = getReservations();
        ObservableList<Reservation> filteredReservation = FXCollections.observableArrayList();

        for (Reservation reservation : allReservation) {
            if (reservation.getSession().toLowerCase().contains(keyword.toLowerCase())) {
                filteredReservation.add(reservation);
            }
        }
        table.setItems(filteredReservation);
    }

    public void Session(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Sessions-front.fxml"));
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
