package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;
import sk.upjs.LoggedUser;
import sk.upjs.dao.ProjectDao;
import sk.upjs.entity.Project;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;
import sk.upjs.models.ProjectFxModel;

import java.util.Optional;

import static sk.upjs.controllers.ProjectsController.projectsMenuClick;

public class ProjectEditController {

    private final User loggedUser = LoggedUser.INSTANCE.getLoggedUser();
    private final ProjectDao projectDao = DaoFactory.INSTANCE.getProjectDao();
    private ProjectFxModel model;

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

    public ProjectEditController(Project selectedProject) {
        model = new ProjectFxModel(selectedProject);
    }

    public ProjectEditController() {
        model = new ProjectFxModel();
    }

    @FXML
    void deleteProjectButtonClick(ActionEvent event) {
        Stage stage = (Stage) projectNameTextField.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete");
        alert.setHeaderText("You are going to delete this project");
        alert.setContentText("Do you want to delete this project?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            projectDao.delete(model.getProject().getId());
        }
        projectsMenuClick(event);
    }

    @FXML
    void logoutButtonClick(ActionEvent event) {
        ProjectsController.logout(event);
    }

    @FXML
    void onDescriptionChange(InputMethodEvent event) {

    }

    @FXML
    void onProjectNameChange(ActionEvent event) {

    }

    @FXML
    void onProjectsButtonMenuClick(ActionEvent event) {
        projectsMenuClick(event);
    }

    @FXML
    void onUsersButtonMenuClick(ActionEvent event) {

    }

    @FXML
    void saveProjectButtonClick(ActionEvent event) {
        Project project = model.getProject();
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
        projectDao.save(project);
        projectsMenuClick(event);
    }

    @FXML
    void initialize() {
        loggedUserNameField.setText(loggedUser.getName() + " " + loggedUser.getSurname());

        projectDescriptionTextArea.textProperty().bindBidirectional(model.descriptionProperty());
        projectNameTextField.textProperty().bindBidirectional(model.nameProperty());
    }

}
