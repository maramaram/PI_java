package tn.esprit.javafxproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static java.sql.Date.valueOf;
import static tn.esprit.javafxproject.DBConnection.getCon;

public class SessionController implements Initializable {
Connection con = null;
PreparedStatement st = null;
ResultSet rs = null;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField tCap;

    @FXML
    private TextField tDes;


    @FXML
    private TableColumn<Session, Integer> colcap;
    @FXML
    private TableColumn<Session, String> coldes;

    @FXML
    private TableColumn<Session, Integer> colid;

    @FXML
    private TableView<Session> table;
    int id =0;
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showSessions();
    }
    public ObservableList<Session> getSesions() {
        ObservableList<Session> session = FXCollections.observableArrayList();
        String query = "select * from session";
    con = getCon();
    try {
        st = con.prepareStatement(query);
        rs = st.executeQuery();
        while (rs.next()) {
            Session s = new Session();
            s.setCap(rs.getInt("Cap"));
            s.setDes(rs.getString("Des"));
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
        colid.setCellValueFactory(new PropertyValueFactory<Session,Integer>("ID"));
        colcap.setCellValueFactory(new PropertyValueFactory<Session,Integer>("Cap"));
        coldes.setCellValueFactory(new PropertyValueFactory<Session,String>("Des"));
    }
    @FXML
    void clearfield(ActionEvent event) {
clear();
    }

    @FXML
    void createSession(ActionEvent event) {
        String req = "insert into session(Cap,Des) values (?,?)";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(req);
            st.setInt(1, Integer.parseInt(tCap.getText()));
            st.setString(2,tDes.getText());
            st.executeUpdate();
            clear();
            showSessions();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void getData(MouseEvent event) {
        Session session = table.getSelectionModel().getSelectedItem();
        id = session.getId();
        tCap.setPrefColumnCount(session.getCap());
        tDes.setText(session.getDes());
        btnSave.setDisable(true);
    }

    void clear() {
        tCap.setText(null);
        tDes.setText(null);
        btnSave.setDisable(false);
    }
    @FXML
    void deleteSession(ActionEvent event) {
        String delete = "delete from session where id = ?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(delete);
            st.setInt(1, id);
            st.executeUpdate();
            showSessions();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void updateSession(ActionEvent event) {
         String update = "update session set Cap = ? , Des = ? where id=?";
         con = DBConnection.getCon();
    try {
        st = con.prepareStatement(update);
        System.out.println("Valeur de tCap : " + tCap.getText());
        int capValue;
        if (tCap.getText() != null && !tCap.getText().isEmpty()) {
            capValue = Integer.parseInt(tCap.getText());
        } else {
            System.out.println("Le champ de Cap est vide.");
            return; // Sortir de la méthode ou afficher un message d'erreur
        }

        st.setInt(1, capValue);
        st.setString(2,tDes.getText());
        st.setInt(3,id);
        st.executeUpdate();
        showSessions();
        clear();
    }catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }
}