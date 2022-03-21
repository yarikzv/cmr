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
    @FXML
    public AnchorPane addPodTabAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCountryList.getItems().setAll(countryService.findAll().stream().map(Country::getName).toList());
        new AutoCompleteComboBoxListener<>(cbCountryList);
        tableContextMenuHandler(podListTable);
    }

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
            refresh();
        } else {
            Alerts.errorAlert("Заполните поля!");
        }
    }

    public void updatePlaceOfDeliveryAction(Integer id) {
        String podAddress = tfPlaceOfDeliveryAddress.getText();
        String podCountry = cbCountryList.getValue();
        if (!podAddress.isEmpty() && podCountry != null) {
            PlaceOfDelivery pod = PlaceOfDelivery.builder()
                    .id(id)
                    .address(podAddress)
                    .country(countryService.findCountryByName(podCountry))
                    .build();
            podService.update(pod);
            Alerts.successAlert("Место доставки " + pod.getAddress() + " успешно сохранено в базу данных!");
            refresh();
        } else {
            Alerts.errorAlert("Заполните поля!");
        }
    }

    public void closeButtonAction() {
        podAnchorPane.getScene().getWindow().hide();
    }

    public void getPlaceOfDeliveryListAction() {
        List<PlaceOfDelivery> pods = podService.findAllPlaceOfDelivery();
        fillTableBySearchResult(pods,
                listAddress,
                listCountry,
                podListTable);
    }

    private void refresh() {
        tfPlaceOfDeliveryAddress.clear();
        cbCountryList.setValue(null);
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

    private void editPodAction(PlaceOfDelivery clickedRowPod) {
        tfPlaceOfDeliveryAddress.setText(clickedRowPod.getAddress());
        cbCountryList.setValue(clickedRowPod.getCountry().getName());
        savePlaceOfDeliveryButton.setVisible(false);
        Button updateButton = new Button("Обновить");
        addPodTabAnchorPane.getChildren().addAll(updateButton);
        updateButton.setId("custom-button");
        updateButton.setPrefHeight(30.0);
        updateButton.setPrefWidth(150.0);
        AnchorPane.setLeftAnchor(updateButton, 50.0);
        AnchorPane.setTopAnchor(updateButton, 130.0);
        updateButton.setOnAction(event -> {
            updatePlaceOfDeliveryAction(clickedRowPod.getId());
            updateButton.setVisible(false);
            savePlaceOfDeliveryButton.setVisible(true);
        });
        podTabPane.getSelectionModel().select(addPlaceOfDeliveryTab);
    }

    private void tableContextMenuHandler(TableView<PlaceOfDelivery> podListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem editMI = new MenuItem("Редактировать");
        cm.getItems().add(editMI);
        MenuItem deleteMI = new MenuItem("Удалить");
        cm.getItems().add(deleteMI);
        podListTable.setRowFactory(tv -> {
            TableRow<PlaceOfDelivery> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(podListTable, t.getScreenX(), t.getScreenY());
                    PlaceOfDelivery clickedRowPod = row.getItem();
                    //edit pod
                    editMI.setOnAction(edit -> editPodAction(clickedRowPod));
                    //delete row
                    deleteMI.setOnAction(delete -> {
                        podService.delete(clickedRowPod);
                        getPlaceOfDeliveryListAction();
                    });
                }
            });
            return row;
        });
    }
}
