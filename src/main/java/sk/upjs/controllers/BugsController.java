package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sk.upjs.LoggedUser;
import sk.upjs.dao.BugDao;
import sk.upjs.dao.ProjectDao;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.*;
import sk.upjs.factory.DaoFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static sk.upjs.controllers.ProjectsController.logout;
import static sk.upjs.controllers.ProjectsController.projectsMenuClick;
import static sk.upjs.controllers.UsersController.usersMenuClick;

public class BugsController {

    private final BugDao bugDao = DaoFactory.INSTANCE.getBugDao();

    private final UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    private final ProjectDao projectDao = DaoFactory.INSTANCE.getProjectDao();

    private final User loggedUser = LoggedUser.INSTANCE.getLoggedUser();

    private ObservableList<Bug> bugsList;

    private ObservableList<Bug> bugsFilteredList;

    private Project filterSelectedProject;

    private Status filterSelectedStatus;

    private Severity filterSelectedSeverity;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MFXButton addBugButton;

    @FXML
    private TableColumn<Bug, String> assigneeCol;

    @FXML
    private TableColumn<Bug, String> assignerCol;

    @FXML
    private TableColumn<Bug, String> bugDescriptionCol;

    @FXML
    private TableColumn<?, ?> bugIdCol;

    @FXML
    private TableColumn<Bug, String> projectNameCol;

    @FXML
    private MFXButton bugsButtonMenu;

    @FXML
    private TableColumn<?, ?> createdAtCol;

    @FXML
    private MFXButton filterApplyButton;

    @FXML
    private MFXButton filterClearButton;

    @FXML
    private MFXToggleButton filterMyBugsButton;

    @FXML
    private MFXComboBox<Project> filterProjectComboBox;

    @FXML
    private MFXComboBox<Severity> filterSeverityComboBox;

    @FXML
    private MFXComboBox<Status> filterStatusComboBox;

    @FXML
    private MFXTextField filterUsernameField;

    @FXML
    private Label loggedUserNameField;

    @FXML
    private MFXButton logoutButton;

    @FXML
    private MFXButton projectsButtonMenu;

    @FXML
    private MFXLegacyTableView<Bug> bugsTable;

    @FXML
    private TableColumn<Bug, String> severityCol;

    @FXML
    private TableColumn<Bug, String> statusCol;

    @FXML
    private TableColumn<?, ?> updatedAtCol;

    @FXML
    private MFXButton usersButtonMenu;


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
    void logoutButtonClick(ActionEvent event) {
        logout(event);
    }

    @FXML
    void onAddBugButtonClick(ActionEvent event) {
        EditBugController controller = new EditBugController();
        showBugEdit(controller, event);

    }

    void showBugEdit(EditBugController controller, Event event) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("edit-bug-view.fxml"));
            fxmlLoader.setController(controller);
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("sk/upjs/favicon.png"));
            stage.setScene(scene);
            stage.setTitle("Edit bug");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onBugsButtonMenuClick(ActionEvent event) {
        bugsMenuClick(event);
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
    void onFilterApplyButtonClick(ActionEvent event) {
        System.out.println("Filtered list:" + bugsFilteredList);
        bugsFilteredList = FXCollections.observableArrayList(bugsList.stream().filter(
                        bug -> (userDao.getById(bug.getAssigneeId()).getUsername().toLowerCase().contains(filterUsernameField.getText().toLowerCase())
                                || userDao.getById(bug.getAssignerId()).getUsername().toLowerCase().contains(filterUsernameField.getText().toLowerCase()))
                                && (filterSelectedProject == null || bug.getProjectId() == filterSelectedProject.getId())
                                && (filterSelectedStatus == null || bug.getStatusId() == filterSelectedStatus.getId())
                                && (filterSelectedSeverity == null || bug.getSeverityId() == filterSelectedSeverity.getId())
                                && (!filterMyBugsButton.isSelected() || (bug.getAssigneeId() == loggedUser.getId() || bug.getAssignerId() == loggedUser.getId()) == filterMyBugsButton.isSelected()))
                .toList());
        System.out.println(bugsFilteredList);
        bugsTable.setItems(bugsFilteredList);
    }

    @FXML
    void onFilterClearButtonClick(ActionEvent event) {
        filterMyBugsButton.setSelected(true);
        filterSeverityComboBox.clearSelection();
        filterStatusComboBox.clearSelection();
        filterProjectComboBox.clearSelection();
        filterUsernameField.setText("");
        bugsTable.setItems(bugsList);
        filterSeverityComboBox.clearSelection();
        filterStatusComboBox.clearSelection();
        filterProjectComboBox.clearSelection();
        bugsFilteredList = null;
    }


    @FXML
    void initialize() {
        loggedUserNameField.setText(loggedUser.getName() + " " + loggedUser.getSurname());

        bugIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        projectNameCol.setCellValueFactory(p -> {
            SimpleStringProperty str = new SimpleStringProperty();
            str.setValue(projectDao.getById(p.getValue().getProjectId()).getName());
            return str;
        });
        statusCol.setCellValueFactory(p -> {
            SimpleStringProperty str = new SimpleStringProperty();
            str.setValue(bugDao.getStatusById(p.getValue().getStatusId()).getName());
            return str;
        });
        severityCol.setCellValueFactory(p -> {
            SimpleStringProperty str = new SimpleStringProperty();
            str.setValue(bugDao.getSeverityById(p.getValue().getSeverityId()).getName());
            return str;
        });
        bugDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtCol.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        assigneeCol.setCellValueFactory(p -> {
            SimpleStringProperty str = new SimpleStringProperty();
            str.setValue(userDao.getById(p.getValue().getAssigneeId()).getName());
            return str;
        });
        assignerCol.setCellValueFactory(p -> {
            SimpleStringProperty str = new SimpleStringProperty();
            str.setValue(userDao.getById(p.getValue().getAssignerId()).getName());
            return str;
        });

        bugsList = FXCollections.observableArrayList(bugDao.getAll());
        bugsTable.setItems(bugsList);

        ObservableList<Project> projects = FXCollections.observableArrayList(projectDao.getAll());
        filterProjectComboBox.setItems(projects);
        filterSelectedProject = filterProjectComboBox.getSelectedItem();
        filterProjectComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filterSelectedProject = newValue;
            }
        });

        ObservableList<Status> statuses = FXCollections.observableArrayList(bugDao.getAllStatuses());
        filterStatusComboBox.setItems(statuses);
        filterSelectedStatus = filterStatusComboBox.getSelectedItem();
        filterStatusComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filterSelectedStatus = newValue;
            }
        });

        ObservableList<Severity> severities = FXCollections.observableArrayList(bugDao.getAllSeverities());
        filterSeverityComboBox.setItems(severities);
        filterSelectedSeverity = filterSeverityComboBox.getSelectedItem();
        filterSeverityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filterSelectedSeverity = newValue;
            }
        });

        bugsTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Bug>) c -> bugsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Bug selectedBug = c.getList().get(0);
                showBugEdit(new EditBugController(selectedBug), event);
            }
        }));
    }

}
