package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Country;
import dev.zvolinskiy.cmr.entity.Recipient;
import dev.zvolinskiy.cmr.service.CountryService;
import dev.zvolinskiy.cmr.service.RecipientService;
import dev.zvolinskiy.cmr.utils.Alerts;
import dev.zvolinskiy.cmr.utils.AutoCompleteComboBoxListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
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
    public TableColumn<Recipient, String> colAddress;
    @FXML
    public TableColumn<Recipient, String> colCountry;
    @FXML
    public TableView<Recipient> recipientsListTable;
    @FXML
    public Button closeButton;

    public void saveRecipientAction() {
        String recipientName = tfRecipientName.getText();
        String recipientAddress = tfRecipientAddress.getText();
        String recipientCountry = cbCountryList.getValue();

        if (!recipientName.equals("") && !recipientAddress.equals("") && recipientCountry != null) {
            Recipient recipient = Recipient.builder()
                    .name(recipientName)
                    .address(recipientAddress)
                    .country(countryService.findCountryByName(recipientCountry))
                    .build();
            recipientService.saveRecipient(recipient);
            Alerts.successAlert("Отправитель " + recipient.getName() + " успешно сохранен в базу данных!");
        } else {
            Alerts.errorAlert("Заполните поля!");
        }
    }

    public void searchRecipientByNameAction() {
        List<Recipient> recipientsByName = new ArrayList<>();
        recipientsByName.add(recipientService.findRecipientByName(tfRecipientNameSearch.getText()));
        fillTableBySearchResult(recipientsByName,
                colName,
                colAddress,
                colCountry,
                searchResultTable);
        tfRecipientNameSearch.clear();
    }

    public void getRecipientsListAction() {
        List<Recipient> recipients = recipientService.findAllRecipient();
        fillTableBySearchResult(recipients,
                listName,
                listAddress,
                listCountry,
                recipientsListTable);
    }

    private void fillTableBySearchResult(List<Recipient> recipients,
                                         TableColumn<Recipient, String> name,
                                         TableColumn<Recipient, String> address,
                                         TableColumn<Recipient, String> country,
                                         TableView<Recipient> table
    ) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        country.setCellValueFactory(countryName -> new SimpleStringProperty(countryName.getValue().getCountry().getName()));
        table.getItems().setAll(recipients);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCountryList.getItems().setAll(countryService.findAll().stream().map(Country::getName).toList());
        new AutoCompleteComboBoxListener<>(cbCountryList);
    }

    public void closeButtonAction() {
        recipientAnchorPane.getScene().getWindow().hide();
    }
}
