package dev.zvolinskiy.cmr.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainWindowController {

    @FXML
    public Button cmrButton;
    @FXML
    public Button driverButton;
    @FXML
    public TabPane driverTabPane;
    @FXML
    public Button saveDriverButton;
    @FXML
    private Button closeButton;

    public void initialize() {
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }

    public void closeButtonAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void cmrButtonAction() {

    }

    public void driverButtonAction(){
        driverTabPane.setVisible(true);
    }

    public void saveDriverAction() {
        driverTabPane.setVisible(false);
    }
}
