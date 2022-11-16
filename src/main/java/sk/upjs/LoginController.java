package sk.upjs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.security.crypto.bcrypt.BCrypt;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;


public class LoginController {

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label wrongCredentialsLabel;

    @FXML
    void loginUser(ActionEvent event) {
        User user = userDao.getByUsername(usernameTextField.getText());
        if (user == null || !BCrypt.checkpw(passwordField.getText(), user.getPassword())) {
            wrongCredentialsLabel.setVisible(true);
            return;
        }
         LoggedUser.INSTANCE.setLoggedUser(user);
    }

    @FXML
    void initialize() {
        wrongCredentialsLabel.setVisible(false);
    }
}

