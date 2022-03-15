package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.*;
import dev.zvolinskiy.cmr.service.*;
import dev.zvolinskiy.cmr.utils.Alerts;
import dev.zvolinskiy.cmr.utils.AutoCompleteComboBoxListener;
import dev.zvolinskiy.cmr.utils.CmrPdfCreator;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CMRController implements Initializable {
    private final CMRService cmrService;
    private final SenderService senderService;
    private final RecipientService recipientService;
    private final PlaceOfDeliveryService podService;
    private final PlaceOfLoadingService polService;
    private final ContainerService containerService;
    private final DriverService driverService;
    private final MainWindowController mainWindowController;

    @FXML
    public AnchorPane cmrAnchorPane;
    @FXML
    public Button bNewSender;
    @FXML
    public Button bNewRecipient;
    @FXML
    public Button bNewPOL;
    @FXML
    public Button bNewPOD;
    @FXML
    public Button bNewContainer;
    @FXML
    public Button bNewDriver;
    @FXML
    public Button saveCmrButton;
    @FXML
    public Button closeButton;
    @FXML
    public ComboBox<String> cbSenderList;
    @FXML
    public ComboBox<String> cbRecipientList;
    @FXML
    public ComboBox<String> cbPOD;
    @FXML
    public ComboBox<String> cbPOL;
    @FXML
    public ComboBox<String> cbContainerList;
    @FXML
    public ComboBox<String> cbDriverList;
    @FXML
    public DatePicker dpCMRDate;
    @FXML
    public TabPane cmrTabPane;
    @FXML
    public Tab addCmrTab;
    @FXML
    public Tab tableCmrTab;
    @FXML
    public TextArea taDocuments;
    @FXML
    public TextArea taCargoName;
    @FXML
    public TextArea taSenderInstructions;
    @FXML
    public TextField tfOrderNumber;
    @FXML
    public TextField tfCargoQuantity;
    @FXML
    public TextField tfCMRNumber;
    @FXML
    public TextField tfCargoWeight;
    @FXML
    public TextField tfCargoCode;
    @FXML
    public TextField tfIssuePlace;
    @FXML
    public Button getContainerListButton;
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
    public Button createPdfButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSenderList.getItems().setAll(senderService.findAllSenders().stream().map(Sender::getName).toList());
        new AutoCompleteComboBoxListener<>(cbSenderList);
        cbRecipientList.getItems().setAll(recipientService.findAllRecipient().stream().map(Recipient::getName).toList());
        new AutoCompleteComboBoxListener<>(cbRecipientList);
        cbPOD.getItems().setAll(podService.findAllPlaceOfDelivery().stream().map(PlaceOfDelivery::getAddress).toList());
        new AutoCompleteComboBoxListener<>(cbPOD);
        cbPOL.getItems().setAll(polService.findAllPlaceOfLoading().stream().map(PlaceOfLoading::getAddress).toList());
        new AutoCompleteComboBoxListener<>(cbPOL);
        cbContainerList.getItems().setAll(containerService.findAllContainers().stream().map(Container::getNumber).toList());
        new AutoCompleteComboBoxListener<>(cbContainerList);
        cbDriverList.getItems().setAll(driverService.findAllDrivers().stream()
                .map(driver -> driver.getLastName() + " " + driver.getFirstName() + " " + driver.getMiddleName())
                .toList());
        new AutoCompleteComboBoxListener<>(cbDriverList);

        tableContextMenuHandler(cmrListTable);
    }

    private void tableContextMenuHandler(TableView<CMR> cmrListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem editMI = new MenuItem("Редактировать");
        cm.getItems().add(editMI);
        MenuItem viewMI = new MenuItem("Просмотреть");
        cm.getItems().add(viewMI);
        MenuItem deleteMI = new MenuItem("Удалить");
        cm.getItems().add(deleteMI);

        cmrListTable.setRowFactory(tv -> {
            TableRow<CMR> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(cmrListTable, t.getScreenX(), t.getScreenY());
                    CMR clickedRowCmr = row.getItem();
                    //edit cmr
                    editMI.setOnAction(edit -> {
                        editCmrAction(clickedRowCmr);
                    });

                    //view row in PDF
                    viewMI.setOnAction(view -> {
                        try {
                            viewPdf(clickedRowCmr);
                        } catch (IOException e) {
                            Alerts.errorAlert("Не могу заполнить CMR");
                        }
                    });
                    //delete row
                    deleteMI.setOnAction(delete -> {
                        cmrService.deleteCMR(clickedRowCmr);
                        getCmrTableAction();
                    });
                }
            });
            return row;
        });


//        cmrListTable.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
//            if (t.getButton() == MouseButton.SECONDARY) {
//                cm.show(cmrListTable, t.getScreenX(), t.getScreenY());
//
//            }
//        });
//
//        viewMI.setOnAction(t -> {
//            cmrListTable.setRowFactory(tv -> {
//                TableRow<CMR> row = new TableRow<>();
//                row.setOnMouseClicked(event -> {
//                    if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
//                            && event.getClickCount() == 2) {
//
//                        CMR clickedRow = row.getItem();
//                        try {
//                            viewPdf(clickedRow);
//                        } catch (IOException e) {
//                            Alerts.errorAlert("Не могу заполнить CMR");
//                        }
//                    }
//                });
//                return row;
//            });
//            viewPdfAction();
//        });
    }

    private void editCmrAction(CMR clickedRowCmr) {
        tfCMRNumber.setText(clickedRowCmr.getNumber());
        dpCMRDate.setValue(clickedRowCmr.getDate());
        tfOrderNumber.setText(clickedRowCmr.getOrderNumber());
        cbSenderList.setValue(clickedRowCmr.getSender().getName());
        cbRecipientList.setValue(clickedRowCmr.getRecipient().getName());
        cbPOL.setValue(clickedRowCmr.getPlaceOfLoading().getAddress());
        cbPOD.setValue(clickedRowCmr.getPlaceOfDelivery().getAddress());
        taDocuments.setText(clickedRowCmr.getDocuments());
        taCargoName.setText(clickedRowCmr.getCargoName());
        cbContainerList.setValue(clickedRowCmr.getContainer().getNumber());
        tfCargoQuantity.setText(clickedRowCmr.getCargoQuantity());
        tfCargoWeight.setText(clickedRowCmr.getCargoWeight());
        tfCargoCode.setText(clickedRowCmr.getCargoCode());
        taSenderInstructions.setText(clickedRowCmr.getSendersInstructions());
        tfIssuePlace.setText(clickedRowCmr.getPlaceOfIssue());
        cbDriverList.setValue(clickedRowCmr.getDriver().getLastName() + " " + clickedRowCmr.getDriver().getFirstName() + " " + clickedRowCmr.getDriver().getMiddleName());
        cmrTabPane.getSelectionModel().select(addCmrTab);
    }

    public void saveCmrAction() {
        try {
            CMR cmr = fillCmr();
            cmrService.saveCMR(cmr);
            Alerts.successAlert("CMR №" + cmr.getNumber() + " успешно сохранена в базу данных!");
        } catch (Exception e) {
            Alerts.errorAlert("Заполните все поля!");
        }
        refresh();
    }

    public void getCmrTableAction() {
        List<CMR> cmrList = cmrService.findAllCMRs();
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

    public void addNewSender() {
        mainWindowController.senderButtonAction();
        cbSenderList.getItems().setAll(senderService.findAllSenders().stream().map(Sender::getName).toList());
        new AutoCompleteComboBoxListener<>(cbSenderList);
    }

    public void addNewRecipient() {
        mainWindowController.recipientButtonAction();
        cbRecipientList.getItems().setAll(recipientService.findAllRecipient().stream().map(Recipient::getName).toList());
        new AutoCompleteComboBoxListener<>(cbRecipientList);
    }

    public void addNewPOL() {
        mainWindowController.polButtonAction();
        cbPOL.getItems().setAll(polService.findAllPlaceOfLoading().stream().map(PlaceOfLoading::getAddress).toList());
        new AutoCompleteComboBoxListener<>(cbPOL);
    }

    public void addNewPOD() {
        mainWindowController.podButtonAction();
        cbPOD.getItems().setAll(podService.findAllPlaceOfDelivery().stream().map(PlaceOfDelivery::getAddress).toList());
        new AutoCompleteComboBoxListener<>(cbPOD);
    }

    public void addNewContainer() {
        mainWindowController.containerButtonAction();
        cbContainerList.getItems().setAll(containerService.findAllContainers().stream().map(Container::getNumber).toList());
        new AutoCompleteComboBoxListener<>(cbContainerList);
    }

    public void addNewDriver() {
        mainWindowController.driverButtonAction();
        cbDriverList.getItems().setAll(driverService.findAllDrivers().stream()
                .map(driver -> driver.getLastName() + " " + driver.getFirstName() + " " + driver.getMiddleName())
                .toList());
        new AutoCompleteComboBoxListener<>(cbDriverList);
    }

    public void closeButtonAction() {
        cleanUpPdf();
        cmrAnchorPane.getScene().getWindow().hide();
    }

    public void viewPdfAction() {
        try {
            CMR cmr = fillCmr();
            viewPdf(cmr);
        } catch (Exception e) {
            Alerts.errorAlert("Не могу заполнить CMR. Проверьте, внесены ли все данные.");
        }
    }

    private void viewPdf(CMR cmr) throws IOException {
        CmrPdfCreator creator = new CmrPdfCreator();
        creator.createPdfFile(cmr);
        File file = new File("CMR" + cmr.getNumber() + ".pdf");
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.getAbsolutePath());

    }

    private CMR fillCmr() throws Exception {
        String cmrNumber = tfCMRNumber.getText();
        LocalDate cmrDate = dpCMRDate.getValue();
        String cmrOrderNumber = tfOrderNumber.getText();
        String cmrSender = cbSenderList.getValue();
        String cmrRecipient = cbRecipientList.getValue();
        String cmrPOD = cbPOD.getValue();
        String cmrPOL = cbPOL.getValue();
        String cmrDocuments = taDocuments.getText();
        String cmrContainer = cbContainerList.getValue();
        String cmrCargoName = taCargoName.getText();
        String cmrCargoQuantity = tfCargoQuantity.getText();
        String cmrCargoWeight = tfCargoWeight.getText();
        String cmrCargoCode = tfCargoCode.getText();
        String cmrSendersInstructions = taSenderInstructions.getText();
        String cmrIssuePlace = tfIssuePlace.getText();
        String cmrDriver = cbDriverList.getValue();

        try {
            return CMR.builder()
                    .number(cmrNumber)
                    .date(cmrDate)
                    .orderNumber(cmrOrderNumber)
                    .sender(senderService.findSenderByName(cmrSender))
                    .recipient(recipientService.findRecipientByName(cmrRecipient))
                    .placeOfDelivery(podService.findPlaceOfDeliveryByAddress(cmrPOD))
                    .placeOfLoading(polService.findPlaceOfLoadingByAddress(cmrPOL))
                    .documents(cmrDocuments)
                    .container(containerService.findContainerByNumber(cmrContainer))
                    .cargoName(cmrCargoName)
                    .cargoQuantity(cmrCargoQuantity)
                    .cargoWeight(cmrCargoWeight)
                    .cargoCode(cmrCargoCode)
                    .sendersInstructions(cmrSendersInstructions)
                    .placeOfIssue(cmrIssuePlace)
                    .driver(driverService.findDriverByFullName(cmrDriver))
                    .build();
        } catch (Exception e) {
            throw new IOException();
        }
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
        date.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        order.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getOrderNumber()));
        sender.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getSender().getName()));
        recipient.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getRecipient().getName()));
        pod.setCellValueFactory(pd -> new SimpleStringProperty(pd.getValue().getPlaceOfDelivery().getAddress()));
        pol.setCellValueFactory(pl -> new SimpleStringProperty(pl.getValue().getPlaceOfLoading().getAddress()));
        documents.setCellValueFactory(doc -> new SimpleStringProperty(doc.getValue().getDocuments()));
        container.setCellValueFactory(cont -> new SimpleStringProperty(cont.getValue().getContainer().getNumber()));
        cargoName.setCellValueFactory(new PropertyValueFactory<>("cargoName"));
        cargoQuantity.setCellValueFactory(new PropertyValueFactory<>("cargoQuantity"));
        cargoWeight.setCellValueFactory(new PropertyValueFactory<>("cargoWeight"));
        cargoCode.setCellValueFactory(new PropertyValueFactory<>("cargoCode"));
        senderInstructions.setCellValueFactory(si -> new SimpleStringProperty(si.getValue().getSendersInstructions()));
        driver.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDriver().getLastName()
                        + " " + d.getValue().getDriver().getFirstName()
                        + " " + d.getValue().getDriver().getMiddleName()));
        truck.setCellValueFactory(trk -> new SimpleStringProperty(trk.getValue().getDriver().getTruck()));
        trailer.setCellValueFactory(trl -> new SimpleStringProperty(trl.getValue().getDriver().getTrailer()));
        table.getItems().setAll(cmrList);
    }

    private void refresh() {
        tfOrderNumber.clear();
        tfCargoQuantity.clear();
        tfCMRNumber.clear();
        tfCargoWeight.clear();
        tfCargoCode.clear();
        tfIssuePlace.clear();
        dpCMRDate.getEditor().clear();
        cbSenderList.setValue(null);
        cbRecipientList.setValue(null);
        cbPOD.setValue(null);
        cbPOL.setValue(null);
        cbContainerList.setValue(null);
        cbDriverList.setValue(null);
        taDocuments.clear();
        taCargoName.clear();
        taSenderInstructions.clear();
    }

    private void cleanUpPdf() {
        File folder = new File(System.getProperty("user.dir"));
        Arrays.stream(folder.listFiles()).filter(f -> f.getName().endsWith(".pdf")).forEach(File::delete);
    }
}
