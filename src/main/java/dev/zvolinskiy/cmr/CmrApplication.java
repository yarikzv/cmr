package dev.zvolinskiy.cmr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CmrApplication extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init(){
        SpringApplication.run(getClass()).getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fx/scene.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm());

            stage.setTitle("CMR - Помощник экспедитора");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setMinHeight(600);
            stage.setMinWidth(800);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
