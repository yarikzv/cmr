package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Country;
import dev.zvolinskiy.cmr.entity.PlaceOfLoading;
import dev.zvolinskiy.cmr.service.CountryService;
import dev.zvolinskiy.cmr.service.PlaceOfLoadingService;
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

    public void savePlaceOfLoadingAction() {
        String polAddress = tfPlaceOfLoadingAddress.getText();
        String polCountry = cbCountryList.getValue();

        PlaceOfLoading pol = PlaceOfLoading.builder()
                .address(polAddress)
                .country(countryService.findCountryByName(polCountry))
                .build();

        polService.savePlaceOfLoading(pol);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Место погрузки " +
                        pol.getAddress() +
                        " успешно сохранено в базу данных!",
                ButtonType.OK);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) alert.close();
        });
        polAnchorPane.getScene().getWindow().hide();
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
}
