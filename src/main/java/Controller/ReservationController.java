package Controller;

import Entities.Reservation;
import Entities.Session;
import Service.ServiceReservation;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.ResourceBundle;

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

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showReservations();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchReservations(newValue));

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
        ObservableList<Reservation> reservations = FXCollections.observableArrayList();
        String query = "SELECT * FROM reservation";
        con = MyDataBase.getInstance().getConnection();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setSession(rs.getString("session"));
                reservation.setDate(String.valueOf(rs.getDate("Date").toLocalDate())); // Convert java.sql.Date to LocalDate
                reservation.setEtat(rs.getString("etat"));
                reservation.setClient(rs.getString("client")); // Ajoutez cette ligne pour récupérer le nom du client
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }


    public void showReservations() {
        ObservableList<Reservation> list = getReservations();
        table.setItems(list);

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
                    System.out.println("Client affiché : " + getItem());
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
    }


    @FXML
    void createReservation(ActionEvent event) {
        // Code pour afficher la vue de création de réservation

        try {
            Parent loader = FXMLLoader.load(getClass().getResource("/Fxml/AjouterReservation.fxml"));
            Stage s;
            Scene scene = new Scene(loader);
            s = (Stage) ((Node) event.getSource()).getScene().getWindow();
            s.setScene(scene);
            s.show();
            showReservations();
        } catch (IOException e) {
            e.printStackTrace();
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
                AfficherSE();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }

    public void AfficherSE() {
        ServiceReservation sr = new ServiceReservation();
        try {
            List<Reservation> l = sr.afficherList();
            table.getItems().clear();
            for (Reservation reservation : l) {
                table.getItems().add(reservation);
            }
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
            client.setCellValueFactory(new PropertyValueFactory<>("client"));
            session.setCellValueFactory(new PropertyValueFactory<Reservation, String>("session"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
}