package dev.zvolinskiy.cmr.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainWindowController implements Initializable {

    private final Resource driverFxml;
    private final Resource senderFxml;
    private final Resource recipientFxml;
    private final Resource podFxml;
    private final Resource polFxml;
    private final Resource containerFxml;
    private final ApplicationContext applicationContext;

    @FXML
    public Button cmrButton;
    @FXML
    public Button driverButton;
    @FXML
    public Button senderButton;
    @FXML
    public Button recipientButton;
    @FXML
    public Button placeOfDeliveryButton;
    @FXML
    public Button placeOfLoadingButton;
    @FXML
    public Button containerButton;
    @FXML
    private Button closeButton;
    @FXML
    private AnchorPane rootAnchorPane;

    public MainWindowController(
            @Value("classpath:/fx/driver.fxml") Resource driverFxml,
            @Value("classpath:/fx/sender.fxml") Resource senderFxml,
            @Value("classpath:/fx/recipient.fxml") Resource recipientFxml,
            @Value("classpath:/fx/pod.fxml") Resource podFxml,
            @Value("classpath:/fx/pol.fxml") Resource polFxml,
            @Value("classpath:/fx/container.fxml") Resource containerFxml,
            ApplicationContext applicationContext) {
        this.driverFxml = driverFxml;
        this.senderFxml = senderFxml;
        this.recipientFxml = recipientFxml;
        this.podFxml = podFxml;
        this.polFxml = polFxml;
        this.containerFxml = containerFxml;
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        driverButton.setOnAction(event -> driverButtonAction());
        senderButton.setOnAction(event -> senderButtonAction());
        recipientButton.setOnAction(event -> recipientButtonAction());
    }

    public void closeButtonAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void cmrButtonAction() {

    }

    public void driverButtonAction() {
        customSceneLoader(driverFxml);
    }

    public void senderButtonAction() {
        customSceneLoader(senderFxml);
    }

    public void recipientButtonAction() {
        customSceneLoader(recipientFxml);
    }

    public void podButtonAction() {
        customSceneLoader(podFxml);
    }
    public void polButtonAction() {
        customSceneLoader(polFxml);
    }

    public void containerButtonAction() {
        customSceneLoader(containerFxml);
    }

    private void customSceneLoader(Resource resourceFxml) {
        try {
            URL url = resourceFxml.getURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setControllerFactory(applicationContext::getBean);
            Node node = loader.load();
            rootAnchorPane.getChildren().set(0, node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
