package sk.upjs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController {
    @FXML
    private PasswordField PasswordTextField;

    @FXML
    private TextField UsernameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label wrongCredentialsLabel;

    @FXML
    void loginUser(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }
}

