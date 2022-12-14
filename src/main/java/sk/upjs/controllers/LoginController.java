package sk.upjs.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import sk.upjs.LoggedUser;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;

import java.io.IOException;

public class LoginController {

    private final UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    @FXML
    private MFXButton loginButton;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXTextField usernameTextField;

    @FXML
    private Label wrongCredentialsLabel;


    @FXML
    void loginUser(ActionEvent event) {
        // dmosko , password
        User user = userDao.getByUsername(usernameTextField.getText());

//        if (user == null || !BCrypt.checkpw(passwordField.getText(), user.getPassword())) {
//            wrongCredentialsLabel.setVisible(true);
//            return;
//        }
        LoggedUser.INSTANCE.setLoggedUser(user);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ProjectsController.class.getResource("project-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Bugster - Projects");
            stage.setScene(scene);
            stage.getIcons().add(new Image("sk/upjs/favicon.png"));
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        wrongCredentialsLabel.setVisible(false);
    }
}