package sk.upjs.bugster.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListController {
    List<String> users = new ArrayList<>();

    @FXML
    private ListView<String> welcomeList;

    @FXML
    private Label welcomeText;

    public ListController() {
        users.add("Jano");
        users.add("Eonasdan");
        users.add("Marcelino");
    }

    @FXML
    void initialize() {
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText(users.toString());
        welcomeList.setItems(FXCollections.observableArrayList(users));
    }

}
