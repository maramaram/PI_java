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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Utils.MyDataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static java.sql.Date.valueOf;

public class SessionController implements Initializable {
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
    private TextField tCap;

    @FXML
    private TextField tDes;

    @FXML
    private TextField tType;
    @FXML
    private ComboBox<String> comboBoxCoach;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableColumn<Session, Integer> colcap;
    @FXML
    private TableColumn<Session, String> coldes;

    @FXML
    private TableColumn<Session, Integer> colid;

    @FXML
    private TableColumn<Session, String> coltype;
    @FXML
    private TableColumn<Session, DatePicker> coldate;
    @FXML
    private TableColumn<Session, String> colcoach;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Session> table;

    int id;
    int myIndex;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showSessions();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchSessions(newValue));
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
                s.setId(rs.getInt("ID"));
                s.setDes(rs.getString("Des"));
                s.setType(rs.getString("Type"));
                s.setCap(Integer.valueOf(rs.getString("Cap")));
                s.setDate(String.valueOf(rs.getDate("Date")));
                s.setCoach(rs.getString("Coach"));
                session.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return session;
    }

    public void showSessions() {
        ObservableList<Session> list = getSesions();
        table.setItems(list);
        colid.setCellValueFactory(new PropertyValueFactory<Session, Integer>("id"));
        colcap.setCellValueFactory(new PropertyValueFactory<Session, Integer>("cap"));
        coldes.setCellValueFactory(new PropertyValueFactory<Session, String>("des"));
        coltype.setCellValueFactory(new PropertyValueFactory<Session, String>("type"));
        coldate.setCellValueFactory(new PropertyValueFactory<Session, DatePicker>("date"));
        colcoach.setCellValueFactory(new PropertyValueFactory<Session,String>("coach"));
    }
    @FXML
    void createSession(ActionEvent event) {
        try {
            Parent loader =  FXMLLoader.load(getClass().getResource("/Fxml/AjouterSession.fxml"));
            Stage s;
            Scene scene=new Scene(loader);
            s=(Stage)((Node)event.getSource()).getScene().getWindow();
            s.setScene(scene);
            s.show();
            showSessions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteSession(ActionEvent event) {
        String delete = "delete from session where id = ?";

        TableView.TableViewSelectionModel<Session> selectionModel=table.getSelectionModel();
        if (!selectionModel.isEmpty())
        {
            ServiceSession es=new ServiceSession();
            Session selectedSession = selectionModel.getSelectedItem();
            try{
                es.supprimer(selectedSession);
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
            showSessions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void searchSessions(String keyword) {

        ObservableList<Session> allSessions = getSesions();
        ObservableList<Session> filteredSessions = FXCollections.observableArrayList();

        for (Session session : allSessions) {
            if (session.getDes().toLowerCase().contains(keyword.toLowerCase())) {
                filteredSessions.add(session);
            }
        }

        table.setItems(filteredSessions);
    }

    public void ajouterNouvelleSession(Date date, String type, Integer capacite, String des, String coach) {
    }

    public void settType(String tType) {
        this.tType.setText(tType);
    }

    public void settCap(String tCap) {
        this.tCap.setText(tCap);
    }

    public void settDes(String tDes) {
        this.tDes.setText(tDes);
    }
    @FXML
    private void sauvegarderSession(ActionEvent event) {
        // Récupérer les données du formulaire
        int capacite = Integer.parseInt(tCap.getText());
        String description = tDes.getText();
        String type = tType.getText();
        String coach = comboBoxCoach.getValue();
        Date date = Date.valueOf(datePicker.getValue());

        // Créer un objet Session avec les données récupérées
        Session session = new Session();

        // Appeler la méthode d'ajout de session du ServiceSession
        ServiceSession serviceSession = new ServiceSession();
        try {
            serviceSession.ajouter(session);
            // Afficher un message de succès
            showSuccessMessage("La session a été ajoutée avec succès !");
            // Mettre à jour la TableView avec les nouvelles données
            showSessions();
        } catch (SQLException e) {
            // En cas d'erreur, afficher un message d'erreur
            showErrorAlert("Échec de l'ajout de la session : " + e.getMessage());
        }
    }

    private void showErrorAlert(String s) {
    }

    private void showSuccessMessage(String s) {
    }
    @FXML
    void refreshSessions() {
        showSessions();
    }
    @FXML
    void sortSessionsAscending(ActionEvent event) {
        ObservableList<Session> sortedList = table.getItems().sorted((s1, s2) -> s1.getDate().compareTo(s2.getDate()));
        table.setItems(sortedList);
    }

    @FXML
    void sortSessionsDescending(ActionEvent event) {
        ObservableList<Session> sortedList = table.getItems().sorted((s1, s2) -> s2.getDate().compareTo(s1.getDate()));
        table.setItems(sortedList);
    }
    public void updateSession(ActionEvent actionEvent) {
    }

    public void updateSession(Session sessionToModify) {
    }
}