package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sk.upjs.LoggedUser;
import sk.upjs.dao.BugDao;
import sk.upjs.dao.ProjectDao;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.*;
import sk.upjs.factory.DaoFactory;
import sk.upjs.models.BugFxModel;

import java.util.Optional;

import static sk.upjs.controllers.BugsController.bugsMenuClick;
import static sk.upjs.controllers.ProjectsController.*;
import static sk.upjs.controllers.UsersController.usersMenuClick;

public class EditBugController {

    private User loggedUser = LoggedUser.INSTANCE.getLoggedUser();
    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private ProjectDao projectDao = DaoFactory.INSTANCE.getProjectDao();
    private BugDao bugDao = DaoFactory.INSTANCE.getBugDao();

    private Status selectedStatus;
    private Severity selectedSeverity;
    private Project selectedProject;

    private java.util.Date dt = new java.util.Date();
    private java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String currentDate = formater.format(dt);

    private BugFxModel bugModel;
    @FXML
    private MFXComboBox<User> assignedUserComboBox;
    @FXML
    private TextArea bugDescription;
    @FXML
    private MFXButton bugsButtonMenu;
    @FXML
    private Label createdAtLabel;
    @FXML
    private MFXButton deleteBugButton;
    @FXML
    private Label loggedUserNameField;
    @FXML
    private MFXButton logoutButton;
    @FXML
    private MFXComboBox<Project> projectComboBox;
    @FXML
    private MFXButton projectsButtonMenu;
    @FXML
    private MFXButton saveBugButton;
    @FXML
    private MFXComboBox<Severity> severityComboBox;
    @FXML
    private MFXComboBox<Status> statusComboBox;
    @FXML
    private Label updatedAtLabel;
    @FXML
    private MFXButton usersButtonMenu;

    public EditBugController() {
        this.bugModel = new BugFxModel();
    }

    public EditBugController(Bug bug) {
        this.bugModel = new BugFxModel(bug);
    }

    @FXML
    void logoutButtonClick(ActionEvent event) {
        logout(event);
    }

    @FXML
    void onBugsButtonMenuClick(ActionEvent event) {
        bugsMenuClick(event);
    }

    @FXML
    void onDeleteBugButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete");
        alert.setHeaderText("You are going to delete this bug");
        alert.setContentText("Do you want to delete this bug?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            bugDao.delete(bugModel.getId());
        }
        bugsMenuClick(event);
    }

    @FXML
    void onProjectsButtonMenuClick(ActionEvent event) {
        projectsMenuClick(event);
    }

    @FXML
    void onSaveBugButtonClick(ActionEvent event) {
        if (bugModel.getId() != null) {
            bugModel.setUpdated_at(currentDate);
        } else {
            bugModel.setCreated_at(currentDate);
            bugModel.setUpdated_at(currentDate);
        }
        Bug bug = bugModel.getBug();
        if (loggedUser.getRole_id() == 3) {
            bugDao.changeStatus(bug.getStatusId(), bug.getId());
        } else {
            bugDao.save(bug);
        }
        System.out.println(bug);
        bugsMenuClick(event);
    }

    @FXML
    void onUsersButtonMenuClick(ActionEvent event) {
        usersMenuClick(event);
    }

    @FXML
    void initialize() {
        loggedUserNameField.setText(loggedUser.getName() + " " + loggedUser.getSurname());


        bugDescription.textProperty().bindBidirectional(bugModel.descriptionProperty());
        assignedUserComboBox.valueProperty().bindBidirectional(bugModel.assigneeProperty());
        projectComboBox.valueProperty().bindBidirectional(bugModel.projectProperty());
        createdAtLabel.textProperty().bindBidirectional(bugModel.created_atProperty());
        updatedAtLabel.textProperty().bindBidirectional(bugModel.updated_atProperty());
        severityComboBox.valueProperty().bindBidirectional(bugModel.severityProperty());
        statusComboBox.valueProperty().bindBidirectional(bugModel.statusProperty());

        ObservableList<Status> statuses = FXCollections.observableArrayList(bugDao.getAllStatuses());
        ObservableList<Severity> severities = FXCollections.observableArrayList(bugDao.getAllSeverities());
        ObservableList<Project> projects = FXCollections.observableArrayList(projectDao.getAll());
        ObservableList<User> users = FXCollections.observableArrayList(userDao.getAll());
        statusComboBox.setItems(statuses);
        severityComboBox.setItems(severities);
        projectComboBox.setItems(projects);
        assignedUserComboBox.setItems(users);

        if (bugModel.getId() != null) {
            //only admin and project manager can edit other than status in bug
            if (!(loggedUser.getRole_id() == 1 || (loggedUser.getRole_id() == 2 &&
                userDao.getByProjectId(bugModel.getProject().getId()).stream().filter(user -> user.getId().equals(loggedUser.getId())).toList().size() > 0))) {
                deleteBugButton.setDisable(true);
                severityComboBox.setDisable(true);
                projectComboBox.setDisable(true);
                assignedUserComboBox.setDisable(true);
                bugDescription.setDisable(true);
            }
            createdAtLabel.setText(bugModel.getCreated_at());
            updatedAtLabel.setText(bugModel.getUpdated_at());
            severityComboBox.selectItem(severities.stream().filter(r -> r.getId() == bugModel.getSeverity().getId()).toList().get(0));
            statusComboBox.selectItem(statuses.stream().filter(r -> r.getId() == bugModel.getStatus().getId()).toList().get(0));
            projectComboBox.selectItem(projects.stream().filter(r -> r.getId().equals(bugModel.getProject().getId())).toList().get(0));
            assignedUserComboBox.selectItem(users.stream().filter(r -> r.getId().equals(bugModel.getAssignee().getId())).toList().get(0));
        } else {
            bugModel.setAssigner(loggedUser);
            deleteBugButton.setVisible(false);
            severityComboBox.selectFirst();
            statusComboBox.selectFirst();
            projectComboBox.selectFirst();
            assignedUserComboBox.selectFirst();
        }
    }
}
