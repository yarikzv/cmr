package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Country;
import dev.zvolinskiy.cmr.entity.Sender;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.service.CountryService;
import dev.zvolinskiy.cmr.service.SenderService;
import dev.zvolinskiy.cmr.utils.Alerts;
import dev.zvolinskiy.cmr.utils.AutoCompleteComboBoxListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class SenderController implements Initializable {
    private final SenderService senderService;
    private final CountryService countryService;

    @FXML
    public AnchorPane senderAnchorPane;
    @FXML
    public TextField tfSenderName;
    @FXML
    public TextField tfSenderAddress;
    @FXML
    public ComboBox<String> cbCountryList;
    @FXML
    public Button saveSenderButton;
    @FXML
    public TabPane senderTabPane;
    @FXML
    public TableView<Sender> searchResultTable;
    @FXML
    public Tab addSenderTab;
    @FXML
    public TextField tfSenderNameSearch;
    @FXML
    public Button searchSenderByName;
    @FXML
    public TableColumn<Sender, String> listName;
    @FXML
    public TableColumn<Sender, String> listAddress;
    @FXML
    public TableColumn<Sender, String> listCountry;
    @FXML
    public Button getSendersListButton;
    @FXML
    public TableColumn<Sender, String> colName;
    @FXML
    public TableColumn<Sender, String> colAddress;
    @FXML
    public TableColumn<Sender, String> colCountry;
    @FXML
    public TableView<Sender> sendersListTable;
    @FXML
    public Button closeButton;
    @FXML
    public AnchorPane addSenderTabAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfSenderName.setOnAction(e -> tfSenderAddress.requestFocus());
        tfSenderAddress.setOnAction(e -> cbCountryList.requestFocus());
        cbCountryList.setOnAction(e -> saveSenderButton.requestFocus());
        cbCountryList.getItems().setAll(countryService.findAll().stream().map(Country::getName).toList());
        new AutoCompleteComboBoxListener<>(cbCountryList);
        tableContextMenuHandler(sendersListTable);
        tableContextMenuHandler(searchResultTable);
    }

    public void saveSenderAction() {
        String senderName = tfSenderName.getText();
        String senderAddress = tfSenderAddress.getText();
        String senderCountry = cbCountryList.getValue();
        if (!senderName.isEmpty() && senderCountry != null) {
            Sender sender = Sender.builder()
                    .name(senderName)
                    .address(senderAddress)
                    .country(countryService.findCountryByName(senderCountry))
                    .build();
            senderService.save(sender);
            Alerts.successAlert("Отправитель " + sender.getName() + " успешно сохранен в базу данных!");
            refresh();
        } else {
            Alerts.errorAlert("Заполните все поля!");
        }
    }

    public void updateSenderAction(Integer id) {
        String senderName = tfSenderName.getText();
        String senderAddress = tfSenderAddress.getText();
        String senderCountry = cbCountryList.getValue();
        if (!senderName.isEmpty() && senderCountry != null) {
            Sender sender = Sender.builder()
                    .id(id)
                    .name(senderName)
                    .address(senderAddress)
                    .country(countryService.findCountryByName(senderCountry))
                    .build();
            senderService.update(sender);
            Alerts.successAlert("Отправитель " + sender.getName() + " успешно обновлен в базу данных!");
            refresh();
        } else {
            Alerts.errorAlert("Заполните все поля!");
        }
    }

    public void closeButtonAction() {
        senderAnchorPane.getScene().getWindow().hide();
    }

    public void searchSenderByNameAction() {
        List<Sender> sendersByName = new ArrayList<>();
        try {
            sendersByName.add(senderService.findByName(tfSenderNameSearch.getText()));
        } catch (Exception e) {
            Alerts.errorAlert("Данные не найдены");
        }
        fillTableBySearchResult(sendersByName,
                colName,
                colAddress,
                colCountry,
                searchResultTable);
        tfSenderNameSearch.clear();
    }

    public void getSendersListAction() {
        List<Sender> senders = senderService.findAll();
        fillTableBySearchResult(senders,
                listName,
                listAddress,
                listCountry,
                sendersListTable);
    }

    private void refresh() {
        tfSenderName.clear();
        tfSenderAddress.clear();
        cbCountryList.setValue(null);
    }

    private void fillTableBySearchResult(List<Sender> senders,
                                         TableColumn<Sender, String> name,
                                         TableColumn<Sender, String> address,
                                         TableColumn<Sender, String> country,
                                         TableView<Sender> table
    ) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        country.setCellValueFactory(countryName -> new SimpleStringProperty(countryName.getValue().getCountry().getName()));
        table.getItems().setAll(senders);
    }

    private void editSenderAction(Sender clickedRowSender) {
        tfSenderName.setText(clickedRowSender.getName());
        tfSenderAddress.setText(clickedRowSender.getAddress());
        cbCountryList.setValue(clickedRowSender.getCountry().getName());
        saveSenderButton.setVisible(false);
        Button updateButton = new Button("Обновить");
        addSenderTabAnchorPane.getChildren().addAll(updateButton);
        updateButton.setId("custom-button");
        updateButton.setPrefHeight(30.0);
        updateButton.setPrefWidth(150.0);
        AnchorPane.setLeftAnchor(updateButton, 50.0);
        AnchorPane.setTopAnchor(updateButton, 200.0);
        updateButton.setOnAction(event -> {
            updateSenderAction(clickedRowSender.getId());
            updateButton.setVisible(false);
            saveSenderButton.setVisible(true);
        });
        senderTabPane.getSelectionModel().select(addSenderTab);
    }

    private void tableContextMenuHandler(TableView<Sender> senderListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem editMI = new MenuItem("Редактировать");
        cm.getItems().add(editMI);
        MenuItem deleteMI = new MenuItem("Удалить");
        cm.getItems().add(deleteMI);
        senderListTable.setRowFactory(tv -> {
            TableRow<Sender> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(senderListTable, t.getScreenX(), t.getScreenY());
                    Sender clickedRowSender = row.getItem();
                    //edit sender
                    editMI.setOnAction(edit -> editSenderAction(clickedRowSender));
                    //delete row
                    deleteMI.setOnAction(delete -> {
                        try {
                            senderService.delete(clickedRowSender);
                        } catch (CmrEntityNotFoundException e) {
                            Alerts.errorAlert("Не удалось удалить. Данные не найдены.");
                        } catch (DataIntegrityViolationException e) {
                            Alerts.errorAlert("Данные используются в другой таблице.");
                        }
                        getSendersListAction();
                    });
                }
            });
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.PRIMARY && t.getClickCount() == 2) {
                    Sender clickedRowSender = row.getItem();
                    editSenderAction(clickedRowSender);
                }
            });
            return row;
        });
    }
}
