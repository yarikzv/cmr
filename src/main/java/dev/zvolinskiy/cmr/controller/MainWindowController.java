package dev.zvolinskiy.cmr.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    public Button cmrButton;
    @FXML
    public Button driverButton;
    @FXML
    private Button closeButton;
    @FXML
    private AnchorPane rootAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        driverButton.setOnAction(event -> {
            driverButtonAction();
        });
    }

    public void closeButtonAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void cmrButtonAction() {

    }

    public void driverButtonAction() {
        try {
            rootAnchorPane.getChildren().set(0, FXMLLoader.load(getClass().getResource("/fx/driver.fxml")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
