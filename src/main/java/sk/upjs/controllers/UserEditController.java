package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import sk.upjs.LoggedUser;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.Role;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;
import sk.upjs.models.UserFxModel;

import java.util.Optional;

import static sk.upjs.controllers.ProjectsController.logout;
import static sk.upjs.controllers.ProjectsController.projectsMenuClick;
import static sk.upjs.controllers.UsersController.usersMenuClick;

public class UserEditController {
    private final User loggedUser = LoggedUser.INSTANCE.getLoggedUser();
    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private ObservableList<Role> roles;
    private Role selectedRole;

    @FXML
    private MFXButton deleteUserButton;

    @FXML
    private MFXToggleButton isActiveButton;

    @FXML
    private Label loggedUserNameField;

    @FXML
    private MFXButton logoutButton;

    @FXML
    private MFXButton projectsButtonMenu;

    @FXML
    private MFXButton saveUserButton;

    @FXML
    private MFXTextField userEmailField;

    @FXML
    private MFXTextField userNameField;

    @FXML
    private MFXPasswordField userPasswordField;

    @FXML
    private MFXComboBox<Role> userRoleComboBox;

    @FXML
    private MFXTextField userSurnameField;

    @FXML
    private MFXTextField userUsernameField;

    @FXML
    private MFXButton usersMenuButtonClick;

    private UserFxModel userModel;

    public UserEditController(User user) {
        this.userModel = new UserFxModel(user);
    }

    public UserEditController() {
        this.userModel = new UserFxModel();
    }

    @FXML
    void logoutButtonClick(ActionEvent event) {
        logout(event);
    }

    @FXML
    void onDeleteUserButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete");
        alert.setHeaderText("You are going to delete this user");
        alert.setContentText("Do you want to delete this user?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            userDao.delete(userModel.getId());
        }
        usersMenuClick(event);
    }

    @FXML
    void onProjectsButtonMenuClick(ActionEvent event) {
        projectsMenuClick(event);
    }

    @FXML
    void onSaveUserButtonClick(ActionEvent event) {
        User user = userModel.getUser();
        if (user.getName() == null || user.getName().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter user name");
            alert.show();
            return;
        }
        if (user.getSurname() == null || user.getSurname().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter user surname");
            alert.show();
            return;
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter username");
            alert.show();
            return;
        }
        if (user.getId() == null && user.getPassword() == null || user.getPassword().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter password");
            alert.show();
            return;
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter email");
            alert.show();
            return;
        }
        userDao.save(user);
        usersMenuClick(event);
    }

    @FXML
    void onUsersButtonMenuClick(ActionEvent event) {
        usersMenuClick(event);
    }


    @FXML
    void initialize() {
        loggedUserNameField.setText(loggedUser.getName() + " " + loggedUser.getSurname());

        userNameField.textProperty().bindBidirectional(userModel.nameProperty());
        userSurnameField.textProperty().bindBidirectional(userModel.surnameProperty());
        userUsernameField.textProperty().bindBidirectional(userModel.usernameProperty());
        userEmailField.textProperty().bindBidirectional(userModel.emailProperty());
        isActiveButton.selectedProperty().bindBidirectional(userModel.activeProperty());

        roles = FXCollections.observableArrayList(userDao.getAllRoles());
        userRoleComboBox.setItems(roles);
        if (userModel.getId() != null) {
            for (Role role : roles) {
                if (role.getId() == userModel.getRole_id()) {
                    selectedRole = role;
                    break;
                }
            }
            userRoleComboBox.selectItem(selectedRole);
        } else {
            userRoleComboBox.selectFirst();
            selectedRole = userRoleComboBox.getSelectedItem();
        }
        userRoleComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Role>() {
            @Override
            public void changed(ObservableValue<? extends Role> observable, Role oldValue, Role newValue) {
                if (newValue != null) {
                    selectedRole = newValue;
                }
            }
        });
    }

}
