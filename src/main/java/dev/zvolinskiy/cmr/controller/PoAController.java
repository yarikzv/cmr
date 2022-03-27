package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.*;
import dev.zvolinskiy.cmr.service.CMRService;
import dev.zvolinskiy.cmr.utils.Alerts;
import dev.zvolinskiy.cmr.utils.pdf.CmrPdfCleaner;
import dev.zvolinskiy.cmr.utils.pdf.CmrPdfCreator;
import dev.zvolinskiy.cmr.utils.pdf.FirmPowerOfAttorneyPdfCreator;
import dev.zvolinskiy.cmr.utils.pdf.MD2PowerOfAttorneyPdfCreator;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PoAController implements Initializable {
    private final CMRService cmrService;
    private final CmrPdfCreator pdfCreator;
    private final MD2PowerOfAttorneyPdfCreator md2PoaCreator;
    private final FirmPowerOfAttorneyPdfCreator blankPoaCreator;

    @FXML
    public AnchorPane poaAnchorPane;
    @FXML
    public TabPane poaTabPane;
    @FXML
    public Tab tableCmrTab;
    @FXML
    public Button getCmrListButton;
    @FXML
    public TableView<CMR> cmrListTable;
    @FXML
    public TableColumn<CMR, String> listNumber;
    @FXML
    public TableColumn<CMR, String> listDate;
    @FXML
    public TableColumn<CMR, String> listOrder;
    @FXML
    public TableColumn<CMR, String> listSender;
    @FXML
    public TableColumn<CMR, String> listRecipient;
    @FXML
    public TableColumn<CMR, String> listPOD;
    @FXML
    public TableColumn<CMR, String> listPOL;
    @FXML
    public TableColumn<CMR, String> listDocuments;
    @FXML
    public TableColumn<CMR, String> listContainer;
    @FXML
    public TableColumn<CMR, String> listCargoName;
    @FXML
    public TableColumn<CMR, String> listCargoQuantity;
    @FXML
    public TableColumn<CMR, String> listCargoWeight;
    @FXML
    public TableColumn<CMR, String> listCargoCode;
    @FXML
    public TableColumn<CMR, String> listSenderInstructions;
    @FXML
    public TableColumn<CMR, String> listDriver;
    @FXML
    public TableColumn<CMR, String> listTruck;
    @FXML
    public TableColumn<CMR, String> listTrailer;
    @FXML
    public Button addBlankButton;
    @FXML
    public AnchorPane poaListAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showUpdateBlankButton();
        tableContextMenuHandler(cmrListTable);
    }

    public void getCmrTableAction() {
        List<CMR> cmrList = cmrService.findAll();
        fillTableBySearchResult(cmrList,
                listNumber,
                listDate,
                listOrder,
                listSender,
                listRecipient,
                listPOD,
                listPOL,
                listDocuments,
                listContainer,
                listCargoName,
                listCargoQuantity,
                listCargoWeight,
                listCargoCode,
                listSenderInstructions,
                listDriver,
                listTruck,
                listTrailer,
                cmrListTable);
    }

    public void closeButtonAction() {
        CmrPdfCleaner.cleanUpPdf();
        poaAnchorPane.getScene().getWindow().hide();
    }

    private void tableContextMenuHandler(TableView<CMR> cmrListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem poaBlankSubMI = new MenuItem("Бланк предприятия");
        MenuItem poaMd2SubMI = new MenuItem("Бланк МД-2");
        cm.getItems().addAll(poaBlankSubMI, poaMd2SubMI);
        cmrListTable.setRowFactory(tv -> {
            TableRow<CMR> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(cmrListTable, t.getScreenX(), t.getScreenY());
                    CMR clickedRowCmr = row.getItem();
                    //create Power of Attorney
                    poaMd2SubMI.setOnAction(poa -> {
                        try {
                            viewPoaMD2(clickedRowCmr);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Alerts.errorAlert("Не могу заполнить Доверенность.\nПроверьте, внесены ли все данные.");
                        }
                    });
                    poaBlankSubMI.setOnAction(poa -> {
                        try {
                            viewPoaBlank(clickedRowCmr);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Alerts.errorAlert("Не могу заполнить Доверенность.\nПроверьте, внесены ли все данные.");
                        }
                    });
                }
            });
            return row;
        });
    }

    private void showUpdateBlankButton() {
        File file = new File("blank/blank.jpg.");
        if (file.exists()) {
            Button updateButton = new Button("Изменить бланк");
            poaListAnchorPane.getChildren().addAll(updateButton);
            updateButton.setId("custom-button");
            updateButton.setPrefHeight(30.0);
            updateButton.setPrefWidth(150.0);
            AnchorPane.setLeftAnchor(updateButton, 225.0);
            AnchorPane.setTopAnchor(updateButton, 70.0);
            addBlankButton.setVisible(false);
            updateButton.setOnAction(e -> addBlankAction());
        }
    }

    private void viewPoaBlank(CMR cmr) {
        String blankImage;
        File selectedFile = new File("blank/blank.jpg");
        if (!selectedFile.exists()) addBlankAction();
        try {
            blankImage = selectedFile.getAbsolutePath();
            blankPoaCreator.createPoaPdfFile(cmr, blankImage);
            var file = new File("PowerOfAttorneyBlank" + cmr.getNumber() + ".pdf");
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.getAbsolutePath());
        } catch (Exception e) {
            Alerts.errorAlert("Не удалось прочитать файл.");
        }
    }

    public void addBlankAction() {
        Stage stage = (Stage) poaAnchorPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        fileChooser.getExtensionFilters().add(extFilterJPG);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Path destPath = Path.of("blank/blank.jpg");
            if (file.exists()) {
                try {
                    Files.copy(Path.of(file.getAbsolutePath()), destPath, REPLACE_EXISTING);
                    Alerts.successAlert("Бланк успешно сохранен.");
                } catch (IOException ex) {
                    Alerts.errorAlert("Не удалось прочитать файл.");
                }
            }
        } else {
            Alerts.errorAlert("Операция отменена.");
        }
    }

    private void viewPoaMD2(CMR cmr) throws IOException {
        md2PoaCreator.createPoaPdfFile(cmr);
        var file = new File("PowerOfAttorney" + cmr.getNumber() + ".pdf");
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.getAbsolutePath());
    }

    private void viewPdf(CMR cmr) throws IOException {
        pdfCreator.createPdfFile(cmr);
        var file = new File("CMR" + cmr.getNumber() + ".pdf");
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.getAbsolutePath());
    }

    private void fillTableBySearchResult(List<CMR> cmrList,
                                         TableColumn<CMR, String> number,
                                         TableColumn<CMR, String> date,
                                         TableColumn<CMR, String> order,
                                         TableColumn<CMR, String> sender,
                                         TableColumn<CMR, String> recipient,
                                         TableColumn<CMR, String> pod,
                                         TableColumn<CMR, String> pol,
                                         TableColumn<CMR, String> documents,
                                         TableColumn<CMR, String> container,
                                         TableColumn<CMR, String> cargoName,
                                         TableColumn<CMR, String> cargoQuantity,
                                         TableColumn<CMR, String> cargoWeight,
                                         TableColumn<CMR, String> cargoCode,
                                         TableColumn<CMR, String> senderInstructions,
                                         TableColumn<CMR, String> driver,
                                         TableColumn<CMR, String> truck,
                                         TableColumn<CMR, String> trailer,
                                         TableView<CMR> table
    ) {
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        date.setCellValueFactory(d -> {
            LocalDate ld = d.getValue().getDate();
            return ld != null ? new SimpleStringProperty(ld.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))) :
                    new SimpleObjectProperty<>(null);
        });
        order.setCellValueFactory(o -> {
            CMR on = o.getValue();
            return on != null ? new SimpleStringProperty(on.getOrderNumber()) : new SimpleStringProperty("");
        });
        sender.setCellValueFactory(s -> {
            Sender snd = s.getValue().getSender();
            return snd != null ? new SimpleStringProperty(snd.getName()) : new SimpleStringProperty("");
        });
        recipient.setCellValueFactory(r -> {
            Recipient rec = r.getValue().getRecipient();
            return rec != null ? new SimpleStringProperty(rec.getName()) : new SimpleStringProperty("");
        });
        pod.setCellValueFactory(pd -> {
            PlaceOfDelivery pdl = pd.getValue().getPlaceOfDelivery();
            return pdl != null ? new SimpleStringProperty(pdl.getAddress()) : new SimpleStringProperty("");
        });
        pol.setCellValueFactory(pl -> {
            PlaceOfLoading pld = pl.getValue().getPlaceOfLoading();
            return pld != null ? new SimpleStringProperty(pld.getAddress()) : new SimpleStringProperty("");
        });
        documents.setCellValueFactory(new PropertyValueFactory<>("documents"));
        container.setCellValueFactory(cont -> {
            Container cnt = cont.getValue().getContainer();
            return cnt != null ? new SimpleStringProperty(cnt.getNumber()) : new SimpleStringProperty("");
        });
        cargoName.setCellValueFactory(new PropertyValueFactory<>("cargoName"));
        cargoQuantity.setCellValueFactory(new PropertyValueFactory<>("cargoQuantity"));
        cargoWeight.setCellValueFactory(new PropertyValueFactory<>("cargoWeight"));
        cargoCode.setCellValueFactory(new PropertyValueFactory<>("cargoCode"));
        senderInstructions.setCellValueFactory(s -> {
            CMR si = s.getValue();
            return si != null ? new SimpleStringProperty(si.getSendersInstructions()) : new SimpleStringProperty("");
        });
        driver.setCellValueFactory(d -> {
            Driver drv = d.getValue().getDriver();
            return drv != null ?
                    new SimpleStringProperty(drv.getLastName()
                            + " " + drv.getFirstName()
                            + " " + drv.getMiddleName()) : new SimpleStringProperty("");
        });
        truck.setCellValueFactory(trk -> {
            Driver drv = trk.getValue().getDriver();
            return drv != null ?
                    new SimpleStringProperty(drv.getTruck()) : new SimpleStringProperty("");
        });
        trailer.setCellValueFactory(trl -> {
            Driver drv = trl.getValue().getDriver();
            return drv != null ?
                    new SimpleStringProperty(drv.getTrailer()) : new SimpleStringProperty("");
        });
        table.getItems().setAll(cmrList);
    }
}
