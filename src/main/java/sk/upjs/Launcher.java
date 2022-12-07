package sk.upjs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sk.upjs.controllers.LoginController;

import java.io.IOException;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login-view-responsive.fxml"));
        stage.getIcons().add(new Image("sk/upjs/favicon.png"));
        /*
        stage.widthProperty().addListener((o, oldValue, newValue) -> {
            if (newValue.intValue() > 1280) {
                stage.setResizable(false);
                stage.setWidth(1280);
                stage.setResizable(true);
            } else if (newValue.intValue() < 400) {
                stage.setResizable(false);
                stage.setWidth(400);
                stage.setResizable(true);
            }
        });

         */

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bugster - Login");
        stage.setScene(scene);
        stage.show();
    }
}

