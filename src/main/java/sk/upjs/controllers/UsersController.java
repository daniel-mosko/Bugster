package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import sk.upjs.LoggedUser;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;

public class UsersController {

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private User loggedUser = LoggedUser.INSTANCE.getLoggedUser();
    private ObservableList<User> usersList;

    @FXML
    private MFXButton addUserButton;

    @FXML
    private MFXButton addUserButton1;

    @FXML
    private MFXToggleButton filterActiveButton;

    @FXML
    private MFXTextField filterEmailField;

    @FXML
    private MFXTextField filterIdField;

    @FXML
    private MFXTextField filterNameField;

    @FXML
    private MFXComboBox<String> filterRoleComboBox;

    @FXML
    private MFXTextField filterSurnameField;

    @FXML
    private MFXTextField filterUsernameField;

    @FXML
    private Label loggedUserNameField;

    @FXML
    private MFXButton logoutButton;

    @FXML
    private MFXButton projectsButtonMenu;

    @FXML
    private TableColumn<User, Boolean> userActiveCol;

    @FXML
    private TableColumn<User, String> userEmailCol;

    @FXML
    private TableColumn<User, Long> userIdCol;

    @FXML
    private TableColumn<User, String> userNameCol;

    @FXML
    private TableColumn<User, String> userRoleCol;

    @FXML
    private TableColumn<User, String> userSurnameCol;

    @FXML
    private TableColumn<User, String> userUsernameCol;

    @FXML
    private MFXButton usersButtonMenu;

    @FXML
    private MFXLegacyTableView<User> usersTable;

    @FXML
    void addUserButtonClick(ActionEvent event) {

    }

    @FXML
    void filterApplyButtonClick(ActionEvent event) {

    }

    @FXML
    void logoutButtonClick(ActionEvent event) {

    }

    @FXML
    void onProjectsButtonMenuClick(ActionEvent event) {

    }

    @FXML
    void onUsersButtonMenuClick(ActionEvent event) {

    }

    @FXML
    void initialize() {
        loggedUserNameField.setText(loggedUser.getName() + " " + loggedUser.getSurname());
        

        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        userSurnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        userUsernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        userActiveCol.setCellValueFactory(new PropertyValueFactory<>("active"));

        usersList = FXCollections.observableArrayList(userDao.getAll());
        usersTable.getItems().setAll(usersList);

    }

}
