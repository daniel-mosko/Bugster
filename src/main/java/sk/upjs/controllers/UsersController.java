package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sk.upjs.LoggedUser;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.Role;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;

import java.io.IOException;

import static sk.upjs.controllers.ProjectsController.logout;
import static sk.upjs.controllers.ProjectsController.projectsMenuClick;

public class UsersController {

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private User loggedUser = LoggedUser.INSTANCE.getLoggedUser();
    private ObservableList<User> usersList;
    private ObservableList<User> usersFilteredList;
    private ObservableList<Role> roles;
    private Role filterSelectedRole;

    @FXML
    private MFXButton addUserButton;

    @FXML
    private MFXButton filterApplyButton;

    @FXML
    private MFXToggleButton filterActiveButton;

    @FXML
    private MFXTextField filterEmailField;

    @FXML
    private MFXTextField filterNameField;

    @FXML
    private MFXComboBox<Role> filterRoleComboBox;

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
    private MFXButton filterClearButton;

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

    static void usersMenuClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(UsersController.class.getResource("user-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.getIcons().add(new Image("sk/upjs/favicon.png"));
            stage.setTitle("Bugster - Users");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void onAddUserButtonClick(ActionEvent event) {
        UserEditController controller = new UserEditController();
        showUserEdit(controller, event);
    }

    void showUserEdit(UserEditController controller, Event event) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("edit-user-view.fxml"));
            fxmlLoader.setController(controller);
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("sk/upjs/favicon.png"));
            stage.setScene(scene);
            stage.setTitle("Edit user");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onFilterApplyButtonClick(ActionEvent event) {
        System.out.println("Filtered list:" + usersFilteredList);
        usersFilteredList = FXCollections.observableArrayList(usersList.stream().filter(
                        user -> user.getName().toLowerCase().contains(filterNameField.getText().toLowerCase())
                                && user.getSurname().toLowerCase().contains(filterSurnameField.getText().toLowerCase())
                                && user.getUsername().toLowerCase().contains(filterUsernameField.getText().toLowerCase())
                                && user.getEmail().toLowerCase().contains(filterEmailField.getText().toLowerCase())
                                && user.isActive() == filterActiveButton.isSelected()
                                && (filterSelectedRole.getName().equals("Any") || user.getRole() == filterSelectedRole.getId()))
                .toList());
        System.out.println(usersFilteredList);
        usersTable.setItems(usersFilteredList);
    }

    @FXML
    void logoutButtonClick(ActionEvent event) {
        logout(event);
    }

    @FXML
    void onProjectsButtonMenuClick(ActionEvent event) {
        projectsMenuClick(event);
    }

    @FXML
    void onUsersButtonMenuClick(ActionEvent event) {
        usersMenuClick(event);
    }

    @FXML
    void onFilterClearButtonClick(ActionEvent event) {
        filterActiveButton.setSelected(true);
        filterRoleComboBox.clearSelection();
        filterEmailField.setText("");
        filterNameField.setText("");
        filterUsernameField.setText("");
        filterNameField.setText("");
        filterSurnameField.setText("");
        usersTable.setItems(usersList);
        filterRoleComboBox.selectLast();
        usersFilteredList = null;
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
        usersTable.setItems(usersList);

        roles = FXCollections.observableArrayList(userDao.getAllRoles());
        roles.add(new Role(null, "Any"));
        filterRoleComboBox.setItems(roles);
        filterRoleComboBox.selectLast();
        filterSelectedRole = filterRoleComboBox.getSelectedItem();
        filterRoleComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Role>() {
            @Override
            public void changed(ObservableValue<? extends Role> observable, Role oldValue, Role newValue) {
                if (newValue != null) {
                    filterSelectedRole = newValue;
                }
            }
        });
        usersTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<User>() {
            @Override
            public void onChanged(Change<? extends User> c) {
                usersTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2) {
                            User selectedUser = c.getList().get(0);
                            showUserEdit(new UserEditController(selectedUser), event);
                        }
                    }
                });
            }
        });
    }

}
