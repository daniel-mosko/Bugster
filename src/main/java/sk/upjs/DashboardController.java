package sk.upjs;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DashboardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addProjectButton;

    @FXML
    private Button addUserButton;

    @FXML
    private Label loggedUserLabel;

    @FXML
    private ListView<?> projectListView;

    @FXML
    void addProjectButtonClick(ActionEvent event) {

    }

    @FXML
    void addUserButtonClick(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert addProjectButton != null : "fx:id=\"addProjectButton\" was not injected: check your FXML file 'project-view.fxml'.";
        assert addUserButton != null : "fx:id=\"addUserButton\" was not injected: check your FXML file 'project-view.fxml'.";
        assert loggedUserLabel != null : "fx:id=\"loggedUserLabel\" was not injected: check your FXML file 'project-view.fxml'.";
        assert projectListView != null : "fx:id=\"projectListView\" was not injected: check your FXML file 'project-view.fxml'.";

    }

}
