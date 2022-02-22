package dev.zvolinskiy.cmr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

import static dev.zvolinskiy.cmr.CMRApplication.StageReadyEvent;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {
    private final Resource fxml;
    private final ApplicationContext applicationContext;

    public StageInitializer(@Value("classpath:/fx/scene.fxml") Resource fxml, ApplicationContext applicationContext) {
        this.fxml = fxml;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            Stage stage = event.getStage();
            URL url = this.fxml.getURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
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
