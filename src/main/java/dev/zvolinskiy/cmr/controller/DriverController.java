package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.entity.Driver;
import dev.zvolinskiy.cmr.entity.Passport;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.service.DriverService;
import dev.zvolinskiy.cmr.service.PassportService;
import dev.zvolinskiy.cmr.utils.Alerts;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class DriverController implements Initializable {
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
    public TextField tfDriverPassportNumber;
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
    @FXML
    public Tab addDriverTab;
    @FXML
    public AnchorPane addDriverTabAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfDriverLastName.setOnAction(e -> tfDriverFirstName.requestFocus());
        tfDriverFirstName.setOnAction(e -> tfDriverMiddleName.requestFocus());
        tfDriverMiddleName.setOnAction(e -> tfDriverPassportNumber.requestFocus());
        tfDriverPassportNumber.setOnAction(e -> {
            tfDriverPassportNumber.setText(tfDriverPassportNumber.getText().toUpperCase());
            dpDriverPassportDate.requestFocus();
        });
        dpDriverPassportDate.setOnAction(e -> tfDriverPassportIssue.requestFocus());
        tfDriverPassportIssue.setOnAction(e -> tfDriverTruck.requestFocus());
        tfDriverTruck.setOnAction((e -> {
            tfDriverTruck.setText(tfDriverTruck.getText().toUpperCase());
            tfDriverTrailer.requestFocus();
        }));
        tfDriverTrailer.setOnAction(e -> {
            tfDriverTrailer.setText(tfDriverTrailer.getText().toUpperCase());
            saveDriverButton.requestFocus();
        });
        tableContextMenuHandler(driverListTable);
        tableContextMenuHandler(searchResultTable);
    }

    public void saveDriverAction() {
        try {
            var driver = fillDriver();
            driverService.save(driver);
            Alerts.successAlert("Водитель " +
                    driver.getLastName() + " " +
                    (!driver.getFirstName().equals("") ? (driver.getFirstName().charAt(0) + ". ") : " ") +
                    (!driver.getMiddleName().equals("") ? (driver.getMiddleName().charAt(0) + ". ") : " ") +
                    " успешно сохранен в базу данных!");
            refresh();
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Заполните необходимые поля!");
        }
    }

    public void updateDriverAction(Integer driverId) {
        try {
            var driver = fillDriver();
            driver.setId(driverId);
            driverService.update(driver);
            Alerts.successAlert("Водитель " +
                    driver.getLastName() + " " +
                    (!driver.getFirstName().equals("") ? (driver.getFirstName().charAt(0) + ". ") : " ") +
                    (!driver.getMiddleName().equals("") ? (driver.getMiddleName().charAt(0) + ". ") : " ") +
                    " успешно сохранен в базу данных!");
            refresh();
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Заполните необходимые поля!");
        }
    }

    public void closeButtonAction() {
        driverAnchorPane.getScene().getWindow().hide();
    }

    public void searchDriverByLastNameAction() {
        try {
            var driversByLastName = driverService.findByLastName(tfDriverLastNameSearch.getText());
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
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Данные не найдены");
        }
        tfDriverLastNameSearch.clear();
    }

    public void searchDriverByPassportNumberAction() {
        List<Driver> driversByPassport = new ArrayList<>();
        try {
            driversByPassport.add(driverService.findByPassport(tfDriverPassportSearch.getText()));
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Водителей с таким паспортом не найдено");
        }
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
        List<Driver> drivers = driverService.findAll();
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

    private void refresh() {
        tfDriverPassportNumber.clear();
        dpDriverPassportDate.setValue(null);
        tfDriverPassportIssue.clear();
        tfDriverFirstName.clear();
        tfDriverMiddleName.clear();
        tfDriverLastName.clear();
        tfDriverTruck.clear();
        tfDriverTrailer.clear();
    }

    private Driver fillDriver() throws CmrEntityNotFoundException {
        String passportNumber = tfDriverPassportNumber.getText().toUpperCase();
        LocalDate passportDate = dpDriverPassportDate.getValue();
        String passportIssue = tfDriverPassportIssue.getText();
        String firstName = tfDriverFirstName.getText();
        String middleName = tfDriverMiddleName.getText();
        String lastName = tfDriverLastName.getText();
        String truck = tfDriverTruck.getText().toUpperCase();
        String trailer = tfDriverTrailer.getText().toUpperCase();

        try {
            Passport passport = Passport.builder()
                    .number(passportNumber)
                    .date(passportDate)
                    .issue(passportIssue)
                    .build();
            return Driver.builder()
                    .firstName(firstName)
                    .middleName(middleName)
                    .lastName(lastName)
                    .passport(passportService.savePassport(passport))
                    .truck(truck)
                    .trailer(trailer)
                    .build();
        } catch (Exception e) {
            throw new CmrEntityNotFoundException();
        }
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

    private void editDriverAction(Driver clickedRowDriver) {
        tfDriverLastName.setText(clickedRowDriver.getLastName());
        tfDriverFirstName.setText(clickedRowDriver.getFirstName());
        tfDriverMiddleName.setText(clickedRowDriver.getMiddleName());
        tfDriverPassportNumber.setText(clickedRowDriver.getPassport().getNumber());
        tfDriverPassportIssue.setText(clickedRowDriver.getPassport().getIssue());
        dpDriverPassportDate.setValue(clickedRowDriver.getPassport().getDate());
        tfDriverTruck.setText(clickedRowDriver.getTruck());
        tfDriverTrailer.setText(clickedRowDriver.getTrailer());
        saveDriverButton.setVisible(false);
        Button updateButton = new Button("Обновить");
        addDriverTabAnchorPane.getChildren().addAll(updateButton);
        updateButton.setId("custom-button");
        updateButton.setPrefHeight(30.0);
        updateButton.setPrefWidth(150.0);
        AnchorPane.setLeftAnchor(updateButton, 50.0);
        AnchorPane.setTopAnchor(updateButton, 420.0);
        updateButton.setOnAction(event -> {
            updateDriverAction(clickedRowDriver.getId());
            updateButton.setVisible(false);
            saveDriverButton.setVisible(true);
        });
        driverTabPane.getSelectionModel().select(addDriverTab);
    }

    private void tableContextMenuHandler(TableView<Driver> driverListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem editMI = new MenuItem("Редактировать");
        cm.getItems().add(editMI);
        MenuItem deleteMI = new MenuItem("Удалить");
        cm.getItems().add(deleteMI);
        driverListTable.setRowFactory(tv -> {
            TableRow<Driver> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(driverListTable, t.getScreenX(), t.getScreenY());
                    Driver clickedRowDriver = row.getItem();
                    //edit pod
                    editMI.setOnAction(edit -> editDriverAction(clickedRowDriver));
                    //delete row
                    deleteMI.setOnAction(delete -> {
                        try {
                            driverService.delete(clickedRowDriver);
                        } catch (CmrEntityNotFoundException e) {
                            Alerts.errorAlert("Не удалось удалить.");
                        }
                        getDriversListAction();
                    });
                }
            });
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->{
                if (t.getButton() == MouseButton.PRIMARY && t.getClickCount() == 2) {
                    Driver clickedRowDriver = row.getItem();
                    editDriverAction(clickedRowDriver);
                }
            });
            return row;
        });
    }
}
