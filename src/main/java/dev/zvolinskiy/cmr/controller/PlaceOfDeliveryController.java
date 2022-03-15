package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Country;
import dev.zvolinskiy.cmr.entity.PlaceOfDelivery;
import dev.zvolinskiy.cmr.service.CountryService;
import dev.zvolinskiy.cmr.service.PlaceOfDeliveryService;
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
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class PlaceOfDeliveryController implements Initializable {
    private final PlaceOfDeliveryService podService;
    private final CountryService countryService;

    @FXML
    public AnchorPane podAnchorPane;
    @FXML
    public TextField tfPlaceOfDeliveryAddress;
    @FXML
    public ComboBox<String> cbCountryList;
    @FXML
    public Button savePlaceOfDeliveryButton;
    @FXML
    public TabPane podTabPane;
    @FXML
    public Tab addPlaceOfDeliveryTab;
    @FXML
    public TableColumn<PlaceOfDelivery, String> listAddress;
    @FXML
    public TableColumn<PlaceOfDelivery, String> listCountry;
    @FXML
    public Button getPlaceOfDeliveryListButton;
    @FXML
    public TableView<PlaceOfDelivery> podListTable;
    @FXML
    public Button closeButton;

    public void savePlaceOfDeliveryAction() {
        String podAddress = tfPlaceOfDeliveryAddress.getText();
        String podCountry = cbCountryList.getValue();

        if (!podAddress.isEmpty() && podCountry != null) {
            PlaceOfDelivery pod = PlaceOfDelivery.builder()
                    .address(podAddress)
                    .country(countryService.findCountryByName(podCountry))
                    .build();
            podService.savePlaceOfDelivery(pod);
            Alerts.successAlert("Место доставки " + pod.getAddress() + " успешно сохранено в базу данных!");
        } else {
            Alerts.errorAlert("Заполните поля!");
        }
    }

    public void getPlaceOfDeliveryListAction() {
        List<PlaceOfDelivery> pods = podService.findAllPlaceOfDelivery();
        fillTableBySearchResult(pods,
                listAddress,
                listCountry,
                podListTable);
    }

    private void fillTableBySearchResult(List<PlaceOfDelivery> pods,
                                         TableColumn<PlaceOfDelivery, String> address,
                                         TableColumn<PlaceOfDelivery, String> country,
                                         TableView<PlaceOfDelivery> table
    ) {
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        country.setCellValueFactory(countryName -> new SimpleStringProperty(countryName.getValue().getCountry().getName()));
        table.getItems().setAll(pods);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCountryList.getItems().setAll(countryService.findAll().stream().map(Country::getName).toList());
        new AutoCompleteComboBoxListener<>(cbCountryList);
    }

    public void closeButtonAction() {
        podAnchorPane.getScene().getWindow().hide();
    }
}
