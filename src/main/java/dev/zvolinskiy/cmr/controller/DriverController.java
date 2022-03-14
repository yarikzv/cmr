package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Driver;
import dev.zvolinskiy.cmr.entity.Passport;
import dev.zvolinskiy.cmr.service.DriverService;
import dev.zvolinskiy.cmr.service.PassportService;
import dev.zvolinskiy.cmr.utils.Alerts;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    @FXML
    public TextField tfDriverLastNameSearch;
    @FXML
    public Button searchDriverByLastName;
    @FXML
    public TextField tfDriverPassportSearch;
    @FXML
    public Button searchDriverByPassportNumber;
    @FXML
    public TableColumn<Driver, String> colLastName;
    @FXML
    public TableColumn<Driver, String> colFirstName;
    @FXML
    public TableColumn<Driver, String> colMiddleName;
    @FXML
    public TableColumn<Driver, String> colPassportNumber;
    @FXML
    public TableColumn<Driver, String> colPassportIssue;
    @FXML
    public TableColumn<Driver, String> colPassportDate;
    @FXML
    public TableColumn<Driver, String> colTruck;
    @FXML
    public TableColumn<Driver, String> colTrailer;
    @FXML
    public TableView<Driver> searchResultTable;
    @FXML
    public Button getDriversListButton;
    @FXML
    public TableView<Driver> driverListTable;
    @FXML
    public TableColumn<Driver, String> listLastName;
    @FXML
    public TableColumn<Driver, String> listFirstName;
    @FXML
    public TableColumn<Driver, String> listMiddleName;
    @FXML
    public TableColumn<Driver, String> listPassportNumber;
    @FXML
    public TableColumn<Driver, String> listPassportIssue;
    @FXML
    public TableColumn<Driver, String> listPassportDate;
    @FXML
    public TableColumn<Driver, String> listTruck;
    @FXML
    public TableColumn<Driver, String> listTrailer;
    @FXML
    public Button closeButton;

    public void saveDriverAction() {
        String passportSeries = tfDriverPassportSeries.getText();
        LocalDate passportDate = dpDriverPassportDate.getValue();
        String passportIssue = tfDriverPassportIssue.getText();
        String firstName = tfDriverFirstName.getText();
        String middleName = tfDriverMiddleName.getText();
        String lastName = tfDriverLastName.getText();
        String truck = tfDriverTruck.getText();
        String trailer = tfDriverTrailer.getText();

        if (!passportSeries.equals("")
                && passportDate != null
                && !passportIssue.equals("")
                && !firstName.equals("")
                && !lastName.equals("")
                && !truck.equals("")) {
            Passport passport = Passport.builder()
                    .number(passportSeries)
                    .date(passportDate)
                    .issue(passportIssue)
                    .build();
            Driver driver = Driver.builder()
                    .firstName(firstName)
                    .middleName(middleName)
                    .lastName(lastName)
                    .passport(passportService.savePassport(passport))
                    .truck(truck)
                    .trailer(trailer)
                    .build();
            driverService.saveDriver(driver);
            Alerts.successAlert("Водитель " +
                    driver.getLastName() + " " +
                    (!driver.getFirstName().equals("") ? (driver.getFirstName().charAt(0) + ". ") : " ") +
                    (!driver.getMiddleName().equals("") ? (driver.getMiddleName().charAt(0) + ". ") : " ") +
                    " успешно сохранен в базу данных!");
        } else {
            Alerts.errorAlert("Заполните все поля!");
        }
    }

    public void searchDriverByLastNameAction() {
        List<Driver> driversByLastName = driverService.findDriverByLastName(tfDriverLastNameSearch.getText());
        fillTableBySearchResult(driversByLastName,
                colLastName,
                colFirstName,
                colMiddleName,
                colPassportNumber,
                colPassportIssue,
                colPassportDate,
                colTruck,
                colTrailer,
                searchResultTable);
        tfDriverLastNameSearch.clear();
    }

    public void searchDriverByPassportNumberAction() {
        List<Driver> driversByPassport = List.of(driverService.findDriverByPassport(tfDriverPassportSearch.getText()));
        fillTableBySearchResult(driversByPassport,
                colLastName,
                colFirstName,
                colMiddleName,
                colPassportNumber,
                colPassportIssue,
                colPassportDate,
                colTruck,
                colTrailer,
                searchResultTable);
        tfDriverPassportSearch.clear();
    }

    public void getDriversListAction() {
        List<Driver> drivers = driverService.findAllDrivers();
        fillTableBySearchResult(drivers,
                listLastName,
                listFirstName,
                listMiddleName,
                listPassportNumber,
                listPassportIssue,
                listPassportDate,
                listTruck,
                listTrailer,
                driverListTable);
    }

    private void fillTableBySearchResult(List<Driver> drivers,
                                         TableColumn<Driver, String> lastName,
                                         TableColumn<Driver, String> firstName,
                                         TableColumn<Driver, String> middleName,
                                         TableColumn<Driver, String> passportNumber,
                                         TableColumn<Driver, String> passportIssue,
                                         TableColumn<Driver, String> passportDate,
                                         TableColumn<Driver, String> truck,
                                         TableColumn<Driver, String> trailer,
                                         TableView<Driver> table
    ) {
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        passportNumber.setCellValueFactory(number -> new SimpleStringProperty(number.getValue().getPassport().getNumber()));
        passportIssue.setCellValueFactory(issue -> new SimpleStringProperty(issue.getValue().getPassport().getIssue()));
        passportDate.setCellValueFactory(date -> new SimpleStringProperty(date.getValue().getPassport().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        truck.setCellValueFactory(new PropertyValueFactory<>("truck"));
        trailer.setCellValueFactory(new PropertyValueFactory<>("trailer"));
        table.getItems().setAll(drivers);
    }

    public void closeButtonAction() {
        driverAnchorPane.getScene().getWindow().hide();
    }
}
