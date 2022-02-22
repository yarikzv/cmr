package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Driver;
import dev.zvolinskiy.cmr.entity.Passport;
import dev.zvolinskiy.cmr.service.DriverService;
import dev.zvolinskiy.cmr.service.PassportService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final PassportService passportService;

    @FXML
    public AnchorPane driverAnchorPane;
    @FXML
    public TextField tfDriverLastName;
    @FXML
    public TextField tfDriverFirstName;
    @FXML
    public TextField tfDriverMiddleName;
    @FXML
    public TextField tfDriverPassportSeries;
    @FXML
    public DatePicker dpDriverPassportDate;
    @FXML
    public TextField tfDriverPassportIssue;
    @FXML
    public TextField tfDriverTruck;
    @FXML
    public TextField tfDriverTrailer;
    @FXML
    public Button saveDriverButton;
    @FXML
    public TabPane driverTabPane;

    public void saveDriverAction() {
        String driverPassportSeries = tfDriverPassportSeries.getText();
        LocalDate driverPassportDate = dpDriverPassportDate.getValue();
        String driverPassportIssue = tfDriverPassportIssue.getText();
        String driverFirstName = tfDriverFirstName.getText();
        String driverMiddleName = tfDriverMiddleName.getText();
        String driverLastName = tfDriverLastName.getText();
        String driverTruck = tfDriverTruck.getText();
        String driverTrailer = tfDriverTrailer.getText();

        Passport passport = Passport.builder()
                .number(driverPassportSeries)
                .date(driverPassportDate)
                .issue(driverPassportIssue)
                .build();

        Driver driver = Driver.builder()
                .firstName(driverFirstName)
                .middleName(driverMiddleName)
                .lastName(driverLastName)
                .passport(passportService.savePassport(passport))
                .truck(driverTruck)
                .trailer(driverTrailer)
                .build();

        driverService.saveDriver(driver);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Водитель " + driver.getLastName() + " успешно сохранен в базу данных!",
                ButtonType.OK);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) alert.close();
        });
        refresh();
    }

    private void refresh(){
        tfDriverPassportSeries.clear();
        dpDriverPassportDate.getEditor().clear();
        tfDriverPassportIssue.clear();
        tfDriverFirstName.clear();
        tfDriverMiddleName.clear();
        tfDriverLastName.clear();
        tfDriverTruck.clear();
        tfDriverTrailer.clear();
    }
}
