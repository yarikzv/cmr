package dev.zvolinskiy.cmr.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerts {

    private Alerts() {
    }

    public static void errorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR,
                message,
                ButtonType.OK);
        alert.setTitle("Ошибка!");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) alert.close();
        });
    }

    public static void successAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                message,
                ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) alert.close();
        });
    }
}
