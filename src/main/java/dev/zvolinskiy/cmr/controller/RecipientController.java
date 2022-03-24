package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Country;
import dev.zvolinskiy.cmr.entity.Recipient;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.service.CountryService;
import dev.zvolinskiy.cmr.service.RecipientService;
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
public class RecipientController implements Initializable {
    private final RecipientService recipientService;
    private final CountryService countryService;

    @FXML
    public AnchorPane recipientAnchorPane;
    @FXML
    public TextField tfRecipientName;
    @FXML
    public TextField tfRecipientAddress;
    @FXML
    public ComboBox<String> cbCountryList;
    @FXML
    public Button saveRecipientButton;
    @FXML
    public TabPane recipientTabPane;
    @FXML
    public TableView<Recipient> searchResultTable;
    @FXML
    public Tab addRecipientTab;
    @FXML
    public TextField tfRecipientNameSearch;
    @FXML
    public Button searchRecipientByName;
    @FXML
    public TableColumn<Recipient, String> listCode;
    @FXML
    public TableColumn<Recipient, String> listName;
    @FXML
    public TableColumn<Recipient, String> listAddress;
    @FXML
    public TableColumn<Recipient, String> listCountry;
    @FXML
    public Button getRecipientsListButton;
    @FXML
    public TableColumn<Recipient, String> colName;
    @FXML
    public TableColumn<Recipient, String> colCode;
    @FXML
    public TableColumn<Recipient, String> colAddress;
    @FXML
    public TableColumn<Recipient, String> colCountry;
    @FXML
    public TableView<Recipient> recipientsListTable;
    @FXML
    public Button closeButton;
    @FXML
    public TextField tfRecipientCodeSearch;
    @FXML
    public Button searchRecipientCode;
    @FXML
    public TextField tfRecipientCode;
    @FXML
    public AnchorPane addRecipientTabAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfRecipientCode.setOnAction(e -> tfRecipientName.requestFocus());
        tfRecipientName.setOnAction(e -> tfRecipientAddress.requestFocus());
        tfRecipientAddress.setOnAction(e ->cbCountryList.requestFocus());
        cbCountryList.setOnAction(e -> saveRecipientButton.requestFocus());
        int max = 8;
        tfRecipientCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > max) {
                String copy = newValue.substring(0, max);
                tfRecipientCode.setText(copy);
            }
        });
        cbCountryList.getItems().setAll(countryService.findAll().stream().map(Country::getName).toList());
        new AutoCompleteComboBoxListener<>(cbCountryList);
        tableContextMenuHandler(recipientsListTable);
        tableContextMenuHandler(searchResultTable);
    }

    public void saveRecipientAction() {
        String recipientCode = tfRecipientCode.getText();
        String recipientName = tfRecipientName.getText();
        String recipientAddress = tfRecipientAddress.getText();
        String recipientCountry = cbCountryList.getValue();
        if (!recipientCode.isEmpty() && !recipientName.isEmpty() && recipientCountry != null) {
            Recipient recipient = Recipient.builder()
                    .name(recipientName)
                    .edrpou(recipientCode)
                    .address(recipientAddress)
                    .country(countryService.findCountryByName(recipientCountry))
                    .build();
            try {
                recipientService.findByEdrpou(recipient.getEdrpou());
                Alert alert = new Alert((Alert.AlertType.CONFIRMATION),
                        "Получатель с таким кодом ЄДРПОУ уже существует в базе данных. Желаете перейти к редактированию?",
                        ButtonType.OK,
                        ButtonType.CANCEL);
                alert.setHeaderText(null);
                alert.setTitle("Информация");
                alert.showAndWait().ifPresent((rs -> {
                    if (rs == ButtonType.CANCEL) alert.close();
                    if (rs == ButtonType.OK) {
                        Recipient newRecipient;
                        try {
                            newRecipient = recipientService.findByEdrpou(recipient.getEdrpou());
                            editRecipientAction(newRecipient);
                        } catch (CmrEntityNotFoundException e) {
                            Alerts.errorAlert("Данные не найдены");
                        }
                    }
                }));
            } catch (CmrEntityNotFoundException e) {
                recipientService.save(recipient);
                Alerts.successAlert("Получатель " + recipient.getName() + " успешно сохранен в базу данных!");
                refresh();
            }
        } else {
            Alerts.errorAlert("Заполните все поля!");
        }
    }

    public void updateRecipientAction(Integer id) {
        String recipientCode = tfRecipientCode.getText();
        String recipientName = tfRecipientName.getText();
        String recipientAddress = tfRecipientAddress.getText();
        String recipientCountry = cbCountryList.getValue();
        if (!recipientCode.isEmpty() && !recipientName.isEmpty() && recipientCountry != null) {
            Recipient recipient = Recipient.builder()
                    .id(id)
                    .name(recipientName)
                    .edrpou(recipientCode)
                    .address(recipientAddress)
                    .country(countryService.findCountryByName(recipientCountry))
                    .build();
            recipientService.update(recipient);
            Alerts.successAlert("Получатель " + recipient.getName() + " успешно обновлен!");
            refresh();
        } else {
            Alerts.errorAlert("Заполните обязательные поля!");
        }
    }

    public void closeButtonAction() {
        recipientAnchorPane.getScene().getWindow().hide();
    }

    public void searchRecipientByNameAction() {
        List<Recipient> recipientsByName = new ArrayList<>();
        try {
            var searchedRecipient = recipientService.findByName(tfRecipientNameSearch.getText());
            recipientsByName.add(searchedRecipient);
            fillTableBySearchResult(recipientsByName,
                    colCode,
                    colName,
                    colAddress,
                    colCountry,
                    searchResultTable);
        } catch (Exception e) {
            Alerts.errorAlert("Данные не найдены");
        }
        tfRecipientNameSearch.clear();
    }

    public void searchRecipientByCodeAction() {
        List<Recipient> recipientsByCode = new ArrayList<>();
        try {
            var searchedRecipient = recipientService.findByEdrpou(tfRecipientCodeSearch.getText());
            recipientsByCode.add(searchedRecipient);
            fillTableBySearchResult(recipientsByCode,
                    colCode,
                    colName,
                    colAddress,
                    colCountry,
                    searchResultTable);
        } catch (Exception e) {
            Alerts.errorAlert("Данные не найдены");
        }
        tfRecipientCodeSearch.clear();
    }

    public void getRecipientsListAction() {
        List<Recipient> recipients = recipientService.findAll();
        fillTableBySearchResult(recipients,
                listCode,
                listName,
                listAddress,
                listCountry,
                recipientsListTable);
    }

    private void refresh() {
        tfRecipientCode.clear();
        tfRecipientName.clear();
        tfRecipientAddress.clear();
        cbCountryList.setValue(null);
    }

    private void fillTableBySearchResult(List<Recipient> recipients,
                                         TableColumn<Recipient, String> code,
                                         TableColumn<Recipient, String> name,
                                         TableColumn<Recipient, String> address,
                                         TableColumn<Recipient, String> country,
                                         TableView<Recipient> table
    ) {
        try {
            code.setCellValueFactory(new PropertyValueFactory<>("edrpou"));
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            country.setCellValueFactory(countryName -> new SimpleStringProperty(countryName.getValue().getCountry().getName()));
            table.getItems().setAll(recipients);
        } catch (Exception e) {
            Alerts.errorAlert("Данные не найдены");
        }
    }

    private void editRecipientAction(Recipient clickedRowRecipient) {
        tfRecipientCode.setText(clickedRowRecipient.getEdrpou());
        tfRecipientName.setText(clickedRowRecipient.getName());
        tfRecipientAddress.setText(clickedRowRecipient.getAddress());
        cbCountryList.setValue(clickedRowRecipient.getCountry().getName());
        saveRecipientButton.setVisible(false);
        Button updateButton = new Button("Обновить");
        addRecipientTabAnchorPane.getChildren().addAll(updateButton);
        updateButton.setId("custom-button");
        updateButton.setPrefHeight(30.0);
        updateButton.setPrefWidth(150.0);
        AnchorPane.setLeftAnchor(updateButton, 50.0);
        AnchorPane.setTopAnchor(updateButton, 250.0);
        updateButton.setOnAction(event -> {
            updateRecipientAction(clickedRowRecipient.getId());
            updateButton.setVisible(false);
            saveRecipientButton.setVisible(true);
        });
        recipientTabPane.getSelectionModel().select(addRecipientTab);
    }

    private void tableContextMenuHandler(TableView<Recipient> recipientListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem editMI = new MenuItem("Редактировать");
        cm.getItems().add(editMI);
        MenuItem deleteMI = new MenuItem("Удалить");
        cm.getItems().add(deleteMI);

        recipientListTable.setRowFactory(tv -> {
            TableRow<Recipient> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(recipientListTable, t.getScreenX(), t.getScreenY());
                    Recipient clickedRowRecipient = row.getItem();
                    //edit recipient
                    editMI.setOnAction(edit -> editRecipientAction(clickedRowRecipient));
                    //delete row
                    deleteMI.setOnAction(delete -> {
                        try {
                            recipientService.delete(clickedRowRecipient);
                        } catch (CmrEntityNotFoundException e) {
                            Alerts.errorAlert("Не удалось удалить. Данные не найдены.");
                        } catch (DataIntegrityViolationException e) {
                            Alerts.errorAlert("Данные используются в другой таблице.");
                        }
                        getRecipientsListAction();
                    });
                }
            });
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->{
                if (t.getButton() == MouseButton.PRIMARY && t.getClickCount() == 2) {
                    Recipient clickedRowRecipient = row.getItem();
                    editRecipientAction(clickedRowRecipient);
                }
            });
            return row;
        });
    }
}
