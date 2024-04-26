package Controller;
import helper.AlertHelper;
import Entities.* ;
import Utils.DataBase ;
import io.github.palexdev.materialfx.controls.MFXPagination;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Pagination;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javafx.stage.Window;
import javafx.scene.control.Button;


public class AdminController {

    @FXML
    private TableColumn<User, String> adress;

    @FXML
    private TableColumn<User, String> email;
    @FXML
    private MFXTableColumn<?> mfxid;
    @FXML
    private TableColumn<User, String> date_n;

    @FXML
    private TableColumn<User, String> firstname;

    @FXML
    private TableColumn<User,String> id;

    @FXML
    private TableColumn<User, String> lastname;
    @FXML
    private Label adminID;

    @FXML
    private MFXPagination pages;
    @FXML
    private TableColumn<User, String> phonenumber;

    @FXML
    private TableColumn<User, String> photo;

    @FXML
    private TableColumn<User, String> role;

    @FXML
    private TableColumn<User, String> status;
    @FXML
    private TableColumn<User, String> action;

    @FXML
    private TableView<User> userTableView;
    @FXML
    private Label logout_btn;

    @FXML
    private TextField filterField;

    private ObservableList<User> listM;
    ObservableList<User> dataList;
    Window window;

    public void DisplayUser() {
        firstname.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        lastname.setCellValueFactory(new PropertyValueFactory<>("nom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        date_n.setCellValueFactory(new PropertyValueFactory<>("date_N"));
        phonenumber.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        adress.setCellValueFactory(new PropertyValueFactory<>("adress"));
        photo.setCellValueFactory(new PropertyValueFactory<>("photo"));
        listM = DataBase.getDatauser();
        userTableView.setItems(listM);
    }
    public void initialize() throws SQLException {
        DisplayUser();
        String userId = SessionManager.getInstance().getUserId();
        String id =  SessionManager.getInstance().getUserFront();
        ActivateUserBack();
        InactiveUserBack();
        search_user();
    }

    void search_user() {
        email.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        phonenumber.setCellValueFactory(new PropertyValueFactory<User,String>("num_tel"));
        dataList = DataBase.getDatauser();
        userTableView.setItems(dataList);
        FilteredList<User> filteredData = new FilteredList<>(dataList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(person.getEmail()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (person.getNum_tel().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                }
                else
                    return false;
            });
        });
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(userTableView.comparatorProperty());
        userTableView.setItems(sortedData);
    }
    private void ActivateUserBack() {
        TableColumn<User, Void> activateButtonColumn = new TableColumn<>("ACTIONS");
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = (TableColumn<User, Void> param) -> {
            final TableCell<User, Void> cell = new TableCell<>() {
                private final Button activateButton = new Button("Activate");
                {
                    activateButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation Dialog");
                        alert.setHeaderText("Confirm Account Activation");
                        alert.setContentText("Are you sure you want to activate this account?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            updateUserStatus(user.getId(), "active");
                            DisplayUser();
                        }
                    });
                }
                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(activateButton);
                    }
                }
            };
            return cell;
        };
        activateButtonColumn.setCellFactory(cellFactory);
        userTableView.getColumns().add(activateButtonColumn);
    }
    private void InactiveUserBack() {
        TableColumn<User, Void> activateButtonColumn = new TableColumn<>("ACTIONS");
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = (TableColumn<User, Void> param) -> {
            final TableCell<User, Void> cell = new TableCell<>() {
                private final Button InactivateButton = new Button("Inactivate");
                {
                    InactivateButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation Dialog");
                        alert.setHeaderText("Confirm Account Deactivation");
                        alert.setContentText("Are you sure you want to deactivate this account?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            updateUserStatusInactive(user.getId(), "inactive");
                            DisplayUser();
                        }
                    });
                }
                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(InactivateButton);
                    }
                }
            };
            return cell;
        };
        activateButtonColumn.setCellFactory(cellFactory);
        userTableView.getColumns().add(activateButtonColumn);
    }


    private void updateUserStatus(String userId, String newStatus) {
        try (Connection conn = DataBase.getConnect();
             PreparedStatement ps = conn.prepareStatement("UPDATE user SET status = ? WHERE id = ?")) {
            ps.setString(1, newStatus);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateUserStatusInactive(String userId, String newStatus) {
        try (Connection conn = DataBase.getConnect();
             PreparedStatement ps = conn.prepareStatement("UPDATE user SET status = ? WHERE id = ?")) {
            ps.setString(1, newStatus);
            ps.setString(2, userId);
            ps.executeUpdate();
            DisplayUser();
        } catch (SQLException e) {
            e.printStackTrace();
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
