package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.service.ContainerService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class ContainerController implements Initializable {
    private final ContainerService containerService;

    @FXML
    public AnchorPane containerAnchorPane;
    @FXML
    public TextField tfContainerNumber;
    @FXML
    public TextField tfContainerType;
    @FXML
    public Button saveContainerButton;
    @FXML
    public TabPane containerTabPane;
    @FXML
    public Tab addContainerTab;
    @FXML
    public TableColumn<Container, String> listNumber;
    @FXML
    public TableColumn<Container, String> listType;
    @FXML
    public Button getContainerListButton;
    @FXML
    public TableView<Container> containerListTable;
    public Button closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfContainerNumber.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if (!tfContainerNumber.getText().matches("[A-Za-z]{4}\\d{7}")) {
                    tfContainerNumber.clear();
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Введите правильный номер контейнера в формате ХХХХ0000000",
                            ButtonType.OK);
                    alert.setTitle("Ошибка!");
                    alert.setHeaderText("Ошибка!");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) alert.close();
                    });
                }
            }
        });
    }

    public void saveContainerAction() {
        String containerNumber = tfContainerNumber.getText().toUpperCase();
        String containerType = tfContainerType.getText();
        if (!containerNumber.equals("") && !Objects.equals(containerType, "")) {
            Container container = Container.builder()
                    .number(containerNumber)
                    .type(containerType)
                    .build();
            containerService.saveContainer(container);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Контейнер " +
                            container.getNumber() +
                            " успешно сохранен в базу данных!",
                    ButtonType.OK);
            alert.setTitle("Поздравляю!");
            alert.setHeaderText("Поздравляю!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) alert.close();
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Заполните поля!",
                    ButtonType.OK);
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Ошибка!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) alert.close();
            });
        }
    }

    public void closeButtonAction() {
        containerAnchorPane.getScene().getWindow().hide();
    }

    public void getContainerListAction() {
        List<Container> containers = containerService.findAllContainers();
        fillTableBySearchResult(containers,
                listNumber,
                listType,
                containerListTable);
    }

    private void fillTableBySearchResult(List<Container> containers,
                                         TableColumn<Container, String> number,
                                         TableColumn<Container, String> type,
                                         TableView<Container> table
    ) {
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        table.getItems().setAll(containers);
    }
}
