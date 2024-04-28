package Controller;

import Entities.Reservation;
import Service.ServiceReservation;
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
import Utils.MyDataBase;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    private Button btnUpdate;

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> comboBoxClient;
    @FXML
    private ComboBox<String> comboBoxSession;

    @FXML
    private TextField tEtat;

    @FXML
    private TableColumn<Reservation, Integer> colid;

    @FXML
    private TableColumn<Reservation, Integer> colsession;

    @FXML
    private TableColumn<Reservation, Date> coldate;

    @FXML
    private TableColumn<Reservation, String> coletat;
    int id;
    @FXML
    private TableView<Reservation> table;

    @FXML
    private TextField searchField;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showReservations();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchReservations(newValue));
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
                reservation.setDate(Date.valueOf(String.valueOf(rs.getDate("Date"))));
                reservation.setEtat(rs.getString("etat"));
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
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colsession.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date"));
        coletat.setCellValueFactory(new PropertyValueFactory<>("etat"));
    }

    @FXML
    void createReservation(ActionEvent event) {
        // Code pour afficher la vue de création de réservation

            try {
                Parent loader =  FXMLLoader.load(getClass().getResource("/Fxml/AjouterReservation.fxml"));
                Stage s;
                Scene scene=new Scene(loader);
                s=(Stage)((Node)event.getSource()).getScene().getWindow();
                s.setScene(scene);
                s.show();
                showReservations();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    @FXML
    void deleteReservation(ActionEvent event) {
        // Code pour supprimer une réservation sélectionnée
        String delete = "delete from session where id = ?";

        TableView.TableViewSelectionModel<Reservation> selectionModel=table.getSelectionModel();
        if (!selectionModel.isEmpty())
        {
            ServiceReservation sr=new ServiceReservation();
            Reservation selectedReservation = selectionModel.getSelectedItem();
            try{
                sr.delete(selectedReservation);
            }catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
        con = MyDataBase.getInstance().getConnection();
        try {
            st = con.prepareStatement(delete);
            st.setInt(1, id);
            st.executeUpdate();
            showReservations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    @FXML
    void sortReservationAscending(ActionEvent event) {
        ObservableList<Reservation> sortedList = table.getItems().sorted((s1, s2) -> s1.getDate().compareTo(s2.getDate()));
        table.setItems(sortedList);
    }

    @FXML
    void sortReservationDescending(ActionEvent event) {
        ObservableList<Reservation> sortedList = table.getItems().sorted((s1, s2) -> s2.getDate().compareTo(s1.getDate()));
        table.setItems(sortedList);
    }

    @FXML
    void updateReservation(ActionEvent event) {
        // Code pour mettre à jour une réservation sélectionnée
    }

    private void searchReservations(String keyword) {
        // Code pour rechercher des réservations selon un mot-clé
    }
}
