package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.service.ContainerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContainerController {
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

    public void saveContainerAction() {
        String containerNumber = tfContainerNumber.getText();
        String containerType = tfContainerType.getText();

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
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) alert.close();
        });
        refresh();
    }

    private void refresh() {
        tfContainerNumber.clear();
        tfContainerType.clear();
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
