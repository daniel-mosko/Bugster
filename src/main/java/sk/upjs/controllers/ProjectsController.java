package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sk.upjs.LoggedUser;
import sk.upjs.dao.ProjectDao;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.Project;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static sk.upjs.controllers.UsersController.usersMenuClick;

public class ProjectsController {

    private final User loggedUser = LoggedUser.INSTANCE.getLoggedUser();
    private final ProjectDao projectDao = DaoFactory.INSTANCE.getProjectDao();
    private final UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private ObservableList<Project> projectsModel;
    @FXML
    private MFXButton addProjectButton;

    @FXML
    private TableColumn<User, String> employeesCol;

    @FXML
    private Label loggedUserNameField;

    @FXML
    private MFXButton logoutButton;

    @FXML
    private TableColumn<Project, String> projectDescriptionCol;

    @FXML
    private TableColumn<Project, Void> actionCol;

    @FXML
    private TableColumn<Project, String> projectIdCol;

    @FXML
    private TableColumn<Project, String> projectNameCol;

    @FXML
    private MFXButton projectsButtonMenu;

    @FXML
    private MFXButton bugsButtonMenu;
    @FXML
    private MFXLegacyTableView<Project> projectsTable;

    @FXML
    private MFXTextField searchBox;


    @FXML
    private MFXButton usersButtonMenu;

    static void logout(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login-view.fxml"));
            LoggedUser.INSTANCE.setLoggedUser(null);
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Bugster - Edit project");
            stage.getIcons().add(new Image("sk/upjs/favicon.png"));
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void projectsMenuClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("project-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Projects");
            stage.setScene(scene);
            stage.getIcons().add(new Image("sk/upjs/favicon.png"));
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void bugsMenuClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BugsController.class.getResource("bug-view-borderPane.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Bugs");
            stage.setScene(scene);
            stage.getIcons().add(new Image("sk/upjs/favicon.png"));
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addProjectButtonClick(ActionEvent event) {
        ProjectEditController controller = new ProjectEditController();
        showProjectEdit(controller, event);
    }

    void showProjectEdit(ProjectEditController controller, Event event) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("edit-project-view.fxml"));
            fxmlLoader.setController(controller);
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("sk/upjs/favicon.png"));
            stage.setScene(scene);
            stage.setTitle("Bugster - Edit project");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    void onBugsButtonMenuClick(ActionEvent event) {
        bugsMenuClick(event);
    }

    @FXML
    void initialize() {
        loggedUserNameField.setText(loggedUser.getName() + " " + loggedUser.getSurname());
        List<Project> projects = projectDao.getAll();
        projectsModel = FXCollections.observableArrayList(projects);
        projectIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        projectNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        projectDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        System.out.println(projects);
        projectsTable.getItems().setAll(projectsModel);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> projectsTable.setItems(
                FXCollections.observableArrayList(projectsModel.stream()
                        .filter(project -> (project.getName() + project.getDescription() + project.getId())
                                .contains(newValue)).collect(Collectors.toList())))
        );

        // Selection model
        TableView.TableViewSelectionModel<Project> selectionModel;
        selectionModel = projectsTable.getSelectionModel();
        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);
        ObservableList<Project> selectedItems = selectionModel.getSelectedItems();
        selectedItems.addListener((ListChangeListener<Project>) c -> projectsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Project selectedProject = c.getList().get(0);
                showProjectEdit(new ProjectEditController(selectedProject), event);
            }
        }));
    }

    @FXML
    void logoutButtonClick(ActionEvent event) {
        logout(event);
    }
}
