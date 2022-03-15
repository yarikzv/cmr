package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Country;
import dev.zvolinskiy.cmr.entity.PlaceOfLoading;
import dev.zvolinskiy.cmr.service.CountryService;
import dev.zvolinskiy.cmr.service.PlaceOfLoadingService;
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

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class PlaceOfLoadingController implements Initializable {
    private final PlaceOfLoadingService polService;
    private final CountryService countryService;

    @FXML
    public AnchorPane polAnchorPane;
    @FXML
    public TextField tfPlaceOfLoadingAddress;
    @FXML
    public ComboBox<String> cbCountryList;
    @FXML
    public Button savePlaceOfLoadingButton;
    @FXML
    public TabPane polTabPane;
    @FXML
    public Tab addPlaceOfLoadingTab;
    @FXML
    public TableColumn<PlaceOfLoading, String> listAddress;
    @FXML
    public TableColumn<PlaceOfLoading, String> listCountry;
    @FXML
    public Button getPlaceOfLoadingListButton;
    @FXML
    public TableView<PlaceOfLoading> polListTable;
    @FXML
    public Button closeButton;

    public void savePlaceOfLoadingAction() {
        String polAddress = tfPlaceOfLoadingAddress.getText();
        @NotNull
        String polCountry = cbCountryList.getValue();

        if (!polAddress.isEmpty() && polCountry != null) {
            PlaceOfLoading pol = PlaceOfLoading.builder()
                    .address(polAddress)
                    .country(countryService.findCountryByName(polCountry))
                    .build();
            polService.savePlaceOfLoading(pol);
            Alerts.successAlert("Место погрузки " + pol.getAddress() + " успешно сохранено в базу данных!");
        } else {
            Alerts.errorAlert("Заполните поля!");
        }
    }

    public void getPlaceOfLoadingListAction() {
        List<PlaceOfLoading> pols = polService.findAllPlaceOfLoading();
        fillTableBySearchResult(pols,
                listAddress,
                listCountry,
                polListTable);
    }

    private void fillTableBySearchResult(List<PlaceOfLoading> pols,
                                         TableColumn<PlaceOfLoading, String> address,
                                         TableColumn<PlaceOfLoading, String> country,
                                         TableView<PlaceOfLoading> table
    ) {
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        country.setCellValueFactory(countryName -> new SimpleStringProperty(countryName.getValue().getCountry().getName()));
        table.getItems().setAll(pols);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCountryList.getItems().setAll(countryService.findAll().stream().map(Country::getName).toList());
        new AutoCompleteComboBoxListener<>(cbCountryList);
    }

    public void closeButtonAction() {
        polAnchorPane.getScene().getWindow().hide();
    }
}
