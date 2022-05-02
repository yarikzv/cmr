package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.Terminal;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.service.TerminalService;
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
public class TerminalController implements Initializable {
    private final TerminalService terminalService;

    @FXML
    public AnchorPane terminalAnchorPane;
    @FXML
    public TextField tfTerminalName;
    @FXML
    public Button saveTerminalButton;
    @FXML
    public TabPane terminalTabPane;
    @FXML
    public Tab addTerminalTab;
    @FXML
    public TableColumn<Terminal, String> listName;
    @FXML
    public Button getTerminalListButton;
    @FXML
    public TableView<Terminal> terminalListTable;
    @FXML
    public Button closeButton;
    @FXML
    public AnchorPane addTerminalTabAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableContextMenuHandler(terminalListTable);
    }

    public void saveTerminalAction() {
        try {
            var terminal = fillTerminal();
            terminalService.save(terminal);
            Alerts.successAlert("Терминал " + terminal.getName() + " успешно сохранен в базу данных!");
            refresh();
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Заполните все поля!");
        }
    }

    public void updateTerminalAction(Integer id) {
        try {
            var terminal = fillTerminal();
            terminal.setId(id);
            terminalService.update(terminal);
            Alerts.successAlert("Терминал " + terminal.getName() + " успешно обновлен!");
            refresh();
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Заполните все поля!");
        }
    }

    public void closeButtonAction() {
        terminalAnchorPane.getScene().getWindow().hide();
    }

    public void getTerminalListAction() {
        List<Terminal> terminals = terminalService.findAll();
        fillTableBySearchResult(terminals,
                listName,
                terminalListTable);
        terminalListTable.getSelectionModel().setCellSelectionEnabled(true);
    }

    private void refresh() {
        tfTerminalName.clear();
    }

    private Terminal fillTerminal() throws CmrEntityNotFoundException {
        String terminalName = tfTerminalName.getText().toUpperCase();
        if (!terminalName.isEmpty()) {
            return Terminal.builder()
                    .name(terminalName)
                    .build();
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    private void fillTableBySearchResult(List<Terminal> terminals,
                                         TableColumn<Terminal, String> name,
                                         TableView<Terminal> table
    ) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getItems().setAll(terminals);
    }

    private void editTerminalAction(Terminal clickedRowTerminal) {
        tfTerminalName.setText(clickedRowTerminal.getName());
        saveTerminalButton.setVisible(false);
        Button updateButton = new Button("Обновить");
        addTerminalTabAnchorPane.getChildren().addAll(updateButton);
        updateButton.setId("custom-button");
        updateButton.setPrefHeight(30.0);
        updateButton.setPrefWidth(150.0);
        AnchorPane.setLeftAnchor(updateButton, 50.0);
        AnchorPane.setTopAnchor(updateButton, 130.0);
        updateButton.setOnAction(event -> {
            updateTerminalAction(clickedRowTerminal.getId());
            updateButton.setVisible(false);
            saveTerminalButton.setVisible(true);
        });
        terminalTabPane.getSelectionModel().select(addTerminalTab);
    }

    private void tableContextMenuHandler(TableView<Terminal> terminalListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem editMI = new MenuItem("Редактировать");
        cm.getItems().add(editMI);
        MenuItem deleteMI = new MenuItem("Удалить");
        cm.getItems().add(deleteMI);
        terminalListTable.setRowFactory(tv -> {
            TableRow<Terminal> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(terminalListTable, t.getScreenX(), t.getScreenY());
                    Terminal clickedRowTerminal = row.getItem();
                    //edit pod
                    editMI.setOnAction(edit -> editTerminalAction(clickedRowTerminal));
                    //delete row
                    deleteMI.setOnAction(delete -> {
                        try {
                            terminalService.delete(clickedRowTerminal);
                        } catch (CmrEntityNotFoundException e) {
                            Alerts.errorAlert("Не удалось удалить");
                        }
                        getTerminalListAction();
                    });
                }
            });
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->{
                if (t.getButton() == MouseButton.PRIMARY && t.getClickCount() == 2) {
                    Terminal clickedRowCont = row.getItem();
                    editTerminalAction(clickedRowCont);
                }
            });
            return row;
        });
    }
}
