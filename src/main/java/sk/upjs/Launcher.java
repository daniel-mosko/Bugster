package sk.upjs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sk.upjs.controllers.LoginController;
import sk.upjs.controllers.ProjectsController;

import java.io.IOException;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login-view.fxml"));
        stage.getIcons().add(new Image("sk/upjs/logo.png"));

        //FXMLLoader fxmlLoader = new FXMLLoader(ProjectsController.class.getResource("project-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bugster | Login");
        stage.setScene(scene);
        stage.show();
    }
}

