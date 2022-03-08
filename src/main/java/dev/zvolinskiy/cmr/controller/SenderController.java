package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Country;
import dev.zvolinskiy.cmr.entity.Sender;
import dev.zvolinskiy.cmr.service.CountryService;
import dev.zvolinskiy.cmr.service.SenderService;
import dev.zvolinskiy.cmr.util.AutoCompleteComboBoxListener;
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

    public void saveSenderAction() {
        String senderName = tfSenderName.getText();
        String senderAddress = tfSenderAddress.getText();
        String senderCountry = cbCountryList.getValue();

        Sender sender = Sender.builder()
                .name(senderName)
                .address(senderAddress)
                .country(countryService.findCountryByName(senderCountry))
                .build();

        senderService.saveSender(sender);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Отправитель " +
                        sender.getName() +
                        " успешно сохранен в базу данных!",
                ButtonType.OK);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) alert.close();
        });
        refresh();
    }

    private void refresh() {
        tfSenderName.clear();
        tfSenderAddress.clear();
        cbCountryList.getSelectionModel().clearSelection();
        cbCountryList.setValue(null);
    }

    public void searchSenderByNameAction() {
        List<Sender> sendersByName = new ArrayList<>();
                sendersByName.add(senderService.findSenderByName(tfSenderNameSearch.getText()));
        fillTableBySearchResult(sendersByName,
                colName,
                colAddress,
                colCountry,
                searchResultTable);
        tfSenderNameSearch.clear();
    }

    public void getSendersListAction() {
        List<Sender> senders = senderService.findAllSenders();
        fillTableBySearchResult(senders,
                listName,
                listAddress,
                listCountry,
                sendersListTable);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCountryList.getItems().setAll(countryService.findAll().stream().map(Country::getName).toList());
        new AutoCompleteComboBoxListener<>(cbCountryList);
    }
}
