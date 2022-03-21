package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.service.ContainerService;
import dev.zvolinskiy.cmr.utils.Alerts;
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
    @FXML
    public Button closeButton;
    @FXML
    public AnchorPane addContainerTabAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfContainerNumber.setOnAction(e -> {
            tfContainerNumber.setText(tfContainerNumber.getText().toUpperCase());
            tfContainerType.requestFocus();
        });
        tfContainerType.setOnAction(e -> saveContainerButton.requestFocus());
        tfContainerNumber.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(newValue) && !tfContainerNumber.getText().matches("[A-Za-z]{4}\\d{7}")) {
                tfContainerNumber.clear();
                Alerts.errorAlert("Введите правильный номер контейнера в формате ХХХХ0000000");
            }
        });
        tableContextMenuHandler(containerListTable);
    }

    public void saveContainerAction() {
        try {
            var container = fillContainer();
            containerService.save(container);
            Alerts.successAlert("Контейнер " + container.getNumber() + " успешно сохранен в базу данных!");
            refresh();
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Заполните все поля!");
        }
    }

    public void updateContainerAction(Integer id) {
        try {
            var container = fillContainer();
            container.setId(id);
            containerService.update(container);
            Alerts.successAlert("Контейнер " + container.getNumber() + " успешно обновлен!");
            refresh();
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Заполните все поля!");
        }
    }

    public void closeButtonAction() {
        containerAnchorPane.getScene().getWindow().hide();
    }

    public void getContainerListAction() {
        List<Container> containers = containerService.findAll();
        fillTableBySearchResult(containers,
                listNumber,
                listType,
                containerListTable);
        containerListTable.getSelectionModel().setCellSelectionEnabled(true);
    }

    private void refresh() {
        tfContainerNumber.clear();
        tfContainerType.clear();
    }

    private Container fillContainer() throws CmrEntityNotFoundException {
        String containerNumber = tfContainerNumber.getText().toUpperCase();
        String containerType = tfContainerType.getText();
        if (!containerNumber.isEmpty() && !containerType.isEmpty()) {
            return Container.builder()
                    .number(containerNumber)
                    .type(containerType)
                    .build();
        } else {
            throw new CmrEntityNotFoundException();
        }
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

    private void editPodAction(Container clickedRowContainer) {
        tfContainerNumber.setText(clickedRowContainer.getNumber());
        tfContainerType.setText(clickedRowContainer.getType());
        saveContainerButton.setVisible(false);
        Button updateButton = new Button("Обновить");
        addContainerTabAnchorPane.getChildren().addAll(updateButton);
        updateButton.setId("custom-button");
        updateButton.setPrefHeight(30.0);
        updateButton.setPrefWidth(150.0);
        AnchorPane.setLeftAnchor(updateButton, 50.0);
        AnchorPane.setTopAnchor(updateButton, 130.0);
        updateButton.setOnAction(event -> {
            updateContainerAction(clickedRowContainer.getId());
            updateButton.setVisible(false);
            saveContainerButton.setVisible(true);
        });
        containerTabPane.getSelectionModel().select(addContainerTab);
    }

    private void tableContextMenuHandler(TableView<Container> containerListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem editMI = new MenuItem("Редактировать");
        cm.getItems().add(editMI);
        MenuItem deleteMI = new MenuItem("Удалить");
        cm.getItems().add(deleteMI);
        containerListTable.setRowFactory(tv -> {
            TableRow<Container> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(containerListTable, t.getScreenX(), t.getScreenY());
                    Container clickedRowContainer = row.getItem();
                    //edit pod
                    editMI.setOnAction(edit -> editPodAction(clickedRowContainer));
                    //delete row
                    deleteMI.setOnAction(delete -> {
                        try {
                            containerService.delete(clickedRowContainer);
                        } catch (CmrEntityNotFoundException e) {
                            Alerts.errorAlert("Не удалось удалить");
                        }
                        getContainerListAction();
                    });
                }
            });
            return row;
        });
    }
}
