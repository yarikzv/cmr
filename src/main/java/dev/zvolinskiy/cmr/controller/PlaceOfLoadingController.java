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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    @FXML
    public Button closeButton;
    @FXML
    public AnchorPane addPolTabAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCountryList.getItems().setAll(countryService.findAll().stream().map(Country::getName).toList());
        new AutoCompleteComboBoxListener<>(cbCountryList);
        tableContextMenuHandler(polListTable);
    }

    public void savePlaceOfLoadingAction() {
        String polAddress = tfPlaceOfLoadingAddress.getText();
        String polCountry = cbCountryList.getValue();
        if (!polAddress.isEmpty() && polCountry != null) {
            PlaceOfLoading pol = PlaceOfLoading.builder()
                    .address(polAddress)
                    .country(countryService.findCountryByName(polCountry))
                    .build();
            polService.save(pol);
            Alerts.successAlert("Место погрузки " + pol.getAddress() + " успешно сохранено в базу данных!");
            refresh();
        } else {
            Alerts.errorAlert("Заполните поля!");
        }
    }

    public void updatePlaceOfLoadingAction(Integer id) {
        String polAddress = tfPlaceOfLoadingAddress.getText();
        String polCountry = cbCountryList.getValue();
        if (!polAddress.isEmpty() && polCountry != null) {
            PlaceOfLoading pol = PlaceOfLoading.builder()
                    .id(id)
                    .address(polAddress)
                    .country(countryService.findCountryByName(polCountry))
                    .build();
            polService.save(pol);
            Alerts.successAlert("Место погрузки " + pol.getAddress() + " успешно обновлено в базу данных!");
            refresh();
        } else {
            Alerts.errorAlert("Заполните поля!");
        }
    }

    public void closeButtonAction() {
        polAnchorPane.getScene().getWindow().hide();
    }

    public void getPlaceOfLoadingListAction() {
        List<PlaceOfLoading> pols = polService.findAllPlaceOfLoading();
        fillTableBySearchResult(pols,
                listAddress,
                listCountry,
                polListTable);
    }

    private void refresh() {
        tfPlaceOfLoadingAddress.clear();
        cbCountryList.setValue(null);
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

    private void editPolAction(PlaceOfLoading clickedRowPol) {
        tfPlaceOfLoadingAddress.setText(clickedRowPol.getAddress());
        cbCountryList.setValue(clickedRowPol.getCountry().getName());
        savePlaceOfLoadingButton.setVisible(false);
        Button updateButton = new Button("Обновить");
        addPolTabAnchorPane.getChildren().addAll(updateButton);
        updateButton.setId("custom-button");
        updateButton.setPrefHeight(30.0);
        updateButton.setPrefWidth(150.0);
        AnchorPane.setLeftAnchor(updateButton, 50.0);
        AnchorPane.setTopAnchor(updateButton, 130.0);
        updateButton.setOnAction(event -> {
            updatePlaceOfLoadingAction(clickedRowPol.getId());
            updateButton.setVisible(false);
            savePlaceOfLoadingButton.setVisible(true);
        });
        polTabPane.getSelectionModel().select(addPlaceOfLoadingTab);
    }

    private void tableContextMenuHandler(TableView<PlaceOfLoading> polListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem editMI = new MenuItem("Редактировать");
        cm.getItems().add(editMI);
        MenuItem deleteMI = new MenuItem("Удалить");
        cm.getItems().add(deleteMI);
        polListTable.setRowFactory(tv -> {
            TableRow<PlaceOfLoading> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(polListTable, t.getScreenX(), t.getScreenY());
                    PlaceOfLoading clickedRowPol = row.getItem();
                    //edit pol
                    editMI.setOnAction(edit -> editPolAction(clickedRowPol));
                    //delete row
                    deleteMI.setOnAction(delete -> {
                        polService.delete(clickedRowPol);
                        getPlaceOfLoadingListAction();
                    });
                }
            });
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->{
                if (t.getButton() == MouseButton.PRIMARY && t.getClickCount() == 2) {
                    PlaceOfLoading clickedRowPol = row.getItem();
                    editPolAction(clickedRowPol);
                }
            });
            return row;
        });
    }
}
