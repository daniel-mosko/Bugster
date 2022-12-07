package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ResourceBundle;

public class BugsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MFXButton addBugButton;

    @FXML
    private TableColumn<?, ?> assigneeCol;

    @FXML
    private TableColumn<?, ?> assignerCol;

    @FXML
    private TableColumn<?, ?> bugDescriptionCol;

    @FXML
    private TableColumn<?, ?> bugIdCol;

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
    private MFXTextField filterProjectField;

    @FXML
    private MFXComboBox<?> filterSeverityComboBox;

    @FXML
    private MFXComboBox<?> filterStatusComboBox;

    @FXML
    private MFXTextField filterUsernameField;

    @FXML
    private Label loggedUserNameField;

    @FXML
    private MFXButton logoutButton;

    @FXML
    private TableColumn<?, ?> projectNameCol;

    @FXML
    private MFXButton projectsButtonMenu;

    @FXML
    private MFXLegacyTableView<?> projectsTable;

    @FXML
    private TableColumn<?, ?> severityCol;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableColumn<?, ?> updatedAtCol;

    @FXML
    private MFXButton usersButtonMenu;

    @FXML
    void logoutButtonClick(ActionEvent event) {

    }

    @FXML
    void onAddUserButtonClick(ActionEvent event) {

    }

    @FXML
    void onBugsButtonMenuClick(ActionEvent event) {

    }

    @FXML
    void onFilterApplyButtonClick(ActionEvent event) {

    }

    @FXML
    void onFilterClearButtonClick(ActionEvent event) {

    }

    @FXML
    void onProjectsButtonMenuClick(ActionEvent event) {

    }

    @FXML
    void onUsersButtonMenuClick(ActionEvent event) {
    }

    @FXML
    void initialize() {
    }

}
