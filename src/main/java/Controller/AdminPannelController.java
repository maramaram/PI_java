package Controller;

import Entities.SessionManager;
import Entities.User;
import Service.UserService;
import Utils.DataBase;
import io.github.palexdev.materialfx.controls.MFXPagination;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class AdminPannelController {

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
DataBase connect;
    private UserService us;
    private ObservableList<User> listM;
    ObservableList<User> dataList;

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
    public AdminPannelController() {
        this.us = new UserService(); // Initialize the UserService field
    }

    public void showList() {
        List<User> listUsers = us.getAllUsers();
        ObservableList<User> observableList = FXCollections.observableArrayList(listUsers);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstname.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        lastname.setCellValueFactory(new PropertyValueFactory<>("nom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        date_n.setCellValueFactory(new PropertyValueFactory<>("date_N"));
        phonenumber.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        adress.setCellValueFactory(new PropertyValueFactory<>("adress"));
        photo.setCellValueFactory(new PropertyValueFactory<>("photo"));

        action.setCellFactory(param -> new ActionButtonCell(us, listUsers));
        // Set up pagination
        int itemsPerPage = 8; // Show 8 rows per page
        int pageCount = (int) Math.ceil((double) listUsers.size() / itemsPerPage);
        pages.setMaxPage(pageCount); // Set the max number of pages

        // Handle pagination changes
        pages.currentPageProperty().addListener((observable, oldValue, newValue) -> {
            // Adjust the current page index for zero-based indexing
            int currentPageIndex = newValue.intValue() - 1;
            int fromIndex = currentPageIndex * itemsPerPage;
            int toIndex = Math.min(fromIndex + itemsPerPage, listUsers.size());
            if (fromIndex < listUsers.size()) {
                userTableView.setItems(FXCollections.observableArrayList(listUsers.subList(fromIndex, toIndex)));
            } else {
                // Handle the case where the current page index exceeds the valid range of indices
                // Set the TableView to an empty list when no items should be displayed
                userTableView.setItems(FXCollections.emptyObservableList());
            }
        });
        userTableView.setItems(observableList);

    }
    @FXML
    public void initialize() {
        // Define table columns
        String userId = SessionManager.getInstance().getUserId();

adminID.setText(userId);
 showList();
        search_user();
    }
    @FXML
    public void logout(javafx.scene.input.MouseEvent mouseEvent) {
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

    // Custom ListCell implementation to display user data in a column layout

    static class ActionButtonCell extends TableCell<User, String> {
        private final Button actionButton;
        private final UserService us; // UserService instance
        private List<User> listUsers; // List of users

        public ActionButtonCell(UserService userService, List<User> listUsers) {
            this.us = userService; // Initialize the UserService instance
            this.listUsers = listUsers; // Initialize the list of users
            actionButton = new Button();
            actionButton.setOnAction(event -> {
                User user = listUsers.get(getIndex());
                if (user.getStatus().equals("Active")) {
                    // Deactivate the user
                    us.deactivateUser(user.getId());
                    AdminPannelController adminPannelController = new AdminPannelController();
                    adminPannelController.showList();
                } else {
                    // Activate the user
                    us.activateUser(user.getId());
                    EmailSender.sendActivationEmail(user.getEmail());
                    AdminPannelController adminPannelController = new AdminPannelController();
                    adminPannelController.showList();
                }
                // Refresh the table view to reflect changes
                AdminPannelController adminPannelController = new AdminPannelController();
                adminPannelController.showList();
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                User user = listUsers.get(getIndex());

                Button activateButton = new Button("Activate");
                Button deactivateButton = new Button("Deactivate");

                activateButton.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Confirm Account Activation");
                    alert.setContentText("Are you sure you want to activate this account?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        us.activateUser(user.getId());
                        AdminPannelController adminPannelController = new AdminPannelController();
                        adminPannelController.showList();
                    }
                });

                deactivateButton.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Confirm Account Deactivation");
                    alert.setContentText("Are you sure you want to deactivate this account?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        us.deactivateUser(user.getId());
                        AdminPannelController adminPannelController = new AdminPannelController();
                        adminPannelController.showList();
                    }
                });

                HBox buttonBox = new HBox(activateButton, deactivateButton);
                buttonBox.setSpacing(5); // Adjust spacing between buttons

                if (user.getStatus().equals("active")) {
                    activateButton.setStyle("-fx-background-color: #CCCCCC;"); // Lighter color
                    deactivateButton.setStyle("-fx-background-color: #333333;"); // Darker color
                } else {
                    activateButton.setStyle("-fx-background-color: #333333;"); // Darker color
                    deactivateButton.setStyle("-fx-background-color: #CCCCCC;"); // Lighter color
                }

                setGraphic(buttonBox);
            }
        }

    }
}
