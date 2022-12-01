package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sk.upjs.LoggedUser;
import sk.upjs.dao.ProjectDao;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.Project;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;
import sk.upjs.models.ProjectFxModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sk.upjs.controllers.ProjectsController.projectsMenuClick;
import static sk.upjs.controllers.UsersController.usersMenuClick;

public class ProjectEditController {

    private final User loggedUser = LoggedUser.INSTANCE.getLoggedUser();
    private final ProjectDao projectDao = DaoFactory.INSTANCE.getProjectDao();
    private final UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private final List<User> deletedUsers = new ArrayList<>();
    private final ProjectFxModel projectModel;
    private final List<User> usersToAdd = new ArrayList<>();
    private ObservableList<User> assignedUsers;
    private User selectedUser;
    private ObservableList<User> userListModel;
    private User selectedComboBoxUser;
    @FXML
    private MFXButton addNewProjectButton;
    @FXML
    private MFXButton deleteProjectButton;
    @FXML
    private Label loggedUserNameField;
    @FXML
    private MFXButton logoutButton;
    @FXML
    private TextArea projectDescriptionTextArea;
    @FXML
    private MFXTextField projectNameTextField;
    @FXML
    private MFXButton projectsButtonMenu;
    @FXML
    private MFXButton usersButtonMenu;
    @FXML
    private TableColumn<User, Long> userIdCol;
    @FXML
    private TableColumn<User, String> userNameCol;
    @FXML
    private TableColumn<User, String> userSurnameCol;
    @FXML
    private MFXLegacyTableView<User> usersTable;
    @FXML
    private MFXButton userDeleteButton;
    @FXML
    private MFXButton userAddButton;
    @FXML
    private MFXComboBox<User> userComboBox;

    public ProjectEditController(Project selectedProject) {
        projectModel = new ProjectFxModel(selectedProject);
    }

    public ProjectEditController() {
        projectModel = new ProjectFxModel();
    }

    @FXML
    void deleteProjectButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete");
        alert.setHeaderText("You are going to delete this project");
        alert.setContentText("Do you want to delete this project?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            projectDao.delete(projectModel.getId());
        }
        projectsMenuClick(event);
    }

    @FXML
    void userAddButtonClick(ActionEvent event) {
        boolean insert = true;
        if (selectedComboBoxUser != null) {
            // Check if selected user from combobox is already assigned
            for (User assignedUser : assignedUsers) {
                if (selectedComboBoxUser.getId().equals(assignedUser.getId())) {
                    insert = false;
                    break;
                }
            }
            if (insert) {
                assignedUsers.add(selectedComboBoxUser);
                usersToAdd.add(selectedComboBoxUser);
                /* Remove user from userListModel */
                for (int i = 0; i < userListModel.size(); i++) {
                    if (userListModel.get(i).getId().equals(selectedComboBoxUser.getId())) {
                        userListModel.remove(i);
                        break;
                    }
                }
                /* Remove user from deletedUsers */
                for (int i = 0; i < deletedUsers.size(); i++) {
                    if (deletedUsers.get(i).getId().equals(selectedComboBoxUser.getId())) {
                        deletedUsers.remove(i);
                        break;
                    }
                }
                userComboBox.clearSelection();
                selectedComboBoxUser = null;
            }
        }
    }

    @FXML
    void userDeleteButtonClick(ActionEvent event) {
        if (assignedUsers.size() == 1) {
            selectedUser = assignedUsers.get(0);
        }
        if (selectedUser != null) {
            /* Creating deep copy of selected User, because selected user can change between two for loops... */
            Long selectedUserId = new User(selectedUser).getId();
            deletedUsers.add(selectedUser);
            userListModel.add(selectedUser);
            /* Remove user from assignedUsers */
            for (int i = 0; i < assignedUsers.size(); i++) {
                if (assignedUsers.get(i).getId().equals(selectedUserId)) {
                    assignedUsers.remove(i);
                    break;
                }
            }
            /* Remove user from usersToAdd */
            for (int i = 0; i < usersToAdd.size(); i++) {
                if (usersToAdd.get(i).getId().equals(selectedUserId)) {
                    usersToAdd.remove(i);
                    break;
                }
            }
            selectedUser = null;
        }
    }

    @FXML
    void logoutButtonClick(ActionEvent event) {
        ProjectsController.logout(event);
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
    void saveProjectButtonClick(ActionEvent event) {
        Project project = projectModel.getProject();
        if (project.getName() == null || project.getName().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Name of a project cannot be empty");
            alert.show();
            return;
        }
        if (project.getDescription() == null || project.getDescription().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Description of a project cannot be empty");
            alert.show();
            return;
        }
        for (User deletedUser : deletedUsers) {
            projectDao.deleteUserFromProject(deletedUser.getId(), projectModel.getId());
        }
        for (User userToAdd : usersToAdd) {
            projectDao.addUserToProject(userToAdd.getId(), projectModel.getId());
        }
        System.out.println("Deleted " + deletedUsers);
        System.out.println("Assigned " + assignedUsers);
        System.out.println("ToAdd " + usersToAdd);
        System.out.println("List " + userListModel);
        projectDao.save(project);
        projectsMenuClick(event);
    }

    @FXML
    void initialize() {
        loggedUserNameField.setText(loggedUser.getName() + " " + loggedUser.getSurname());

        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        userSurnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));

        assignedUsers = FXCollections.observableArrayList(userDao.getByProjectId(projectModel.getId()));
        usersTable.setItems(assignedUsers);

        projectDescriptionTextArea.textProperty().bindBidirectional(projectModel.descriptionProperty());
        projectNameTextField.textProperty().bindBidirectional(projectModel.nameProperty());

        // UserComboBox selection model
        userListModel = FXCollections.observableArrayList(userDao.getAll());
        for (int i = 0; i < userListModel.size(); i++) {
            for (User assignedUser : assignedUsers) {
                if (userListModel.get(i).getId().equals(assignedUser.getId())) {
                    userListModel.remove(i);
                }
            }
        }
        userComboBox.setItems(userListModel);

        userComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                if (newValue != null) {
                    selectedComboBoxUser = newValue;
                }
            }
        });


        // Table selection model
        TableView.TableViewSelectionModel<User> selectionModel = usersTable.getSelectionModel();
        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);
        ObservableList<User> selectedItems = selectionModel.getSelectedItems();
        selectedItems.addListener(new ListChangeListener<User>() {
            @Override
            public void onChanged(Change<? extends User> c) {
                if (c.getList().size() > 0) {
                    selectedUser = c.getList().get(0);
                    userDeleteButton.setDisable(loggedUser.getId().equals(selectedUser.getId()));
                    System.out.println(selectedUser);
                }
            }
        });
    }
}