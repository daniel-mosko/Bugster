package sk.upjs.controllers;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import sk.upjs.EmailValidator;
import sk.upjs.LoggedUser;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.Role;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;
import sk.upjs.models.UserFxModel;

import java.util.Optional;

import static sk.upjs.controllers.BugsController.bugsMenuClick;
import static sk.upjs.controllers.ProjectsController.logout;
import static sk.upjs.controllers.ProjectsController.projectsMenuClick;
import static sk.upjs.controllers.UsersController.usersMenuClick;

public class UserEditController {
    private final User loggedUser = LoggedUser.INSTANCE.getLoggedUser();
    private final UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private final UserFxModel userModel;
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
    private MFXButton bugsButtonMenu;
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
    void onBugsButtonMenuClick(ActionEvent event) {
        bugsMenuClick(event);
    }

    @FXML
    void onSaveUserButtonClick(ActionEvent event) {
        User user = userModel.getUser();
        boolean userHasPassword = user.getId() != null && !userDao.getById(user.getId()).getPassword().isBlank();
        // user.setRole_id(selectedRole.getId());
        if (user.getName() == null || user.getName().isBlank() || user.getName().length() > 45) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter user name (max 45 chars)");
            alert.show();
            return;
        }
        if (user.getSurname() == null || user.getSurname().isBlank() || user.getSurname().length() > 45) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter user surname (max 45 chars)");
            alert.show();
            return;
        }
        if (user.getUsername() == null || user.getUsername().isBlank() || user.getUsername().length() <= 3 || user.getUsername().length() > 45) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter valid username (4+ characters and less than 45 chars)");
            alert.show();
            return;
        }
        System.out.println(userHasPassword);
        System.out.println(user.getPassword());
        if (!userHasPassword) {
            if (userPasswordField.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Enter password");
                alert.show();
                return;
            }

            // Check strength of a password
            Zxcvbn zxcvbn = new Zxcvbn();
            Strength strength = zxcvbn.measure(userPasswordField.getText());
            // 2 Good （guesses < 10^8 + 5）
            if (strength.getScore() <= 2) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Weak password");
                alert.show();
                return;
            }
            user.setPassword(userPasswordField.getText());
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !EmailValidator.validate(user.getEmail()) || user.getEmail().length() > 45) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid email (invalid format or length > 45 chars)");
            alert.show();
            return;
        }
        // if updated user is logged user
        if (user.getId() != null && user.getId().equals(loggedUser.getId())) {
            LoggedUser.INSTANCE.setLoggedUser(user);
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
        userRoleComboBox.valueProperty().bindBidirectional(userModel.roleProperty());
        isActiveButton.selectedProperty().bindBidirectional(userModel.activeProperty());

        ObservableList<Role> roles = FXCollections.observableArrayList(userDao.getAllRoles());
        userRoleComboBox.setItems(roles);

        if (userModel.getId() != null) {
            userRoleComboBox.selectItem(roles.stream().filter(r -> r.getId() == userModel.getRole().getId()).toList().get(0));
        } else {
            deleteUserButton.setVisible(false);
            userRoleComboBox.selectFirst();
        }
    }

}
