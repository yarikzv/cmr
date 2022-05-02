package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.*;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.service.*;
import dev.zvolinskiy.cmr.utils.Alerts;
import dev.zvolinskiy.cmr.utils.AutoCompleteComboBoxListener;
import dev.zvolinskiy.cmr.utils.pdf.CmrPdfCleaner;
import dev.zvolinskiy.cmr.utils.pdf.CmrPdfCreator;
import dev.zvolinskiy.cmr.utils.pdf.FirmPowerOfAttorneyPdfCreator;
import dev.zvolinskiy.cmr.utils.pdf.MD2PowerOfAttorneyPdfCreator;
import dev.zvolinskiy.cmr.utils.xmlparser.CmrToXmlCreator;
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final TerminalService terminalService;
    private final MainWindowController mainWindowController;
    private final CmrPdfCreator pdfCreator;
    private final MD2PowerOfAttorneyPdfCreator md2PoaCreator;
    private final FirmPowerOfAttorneyPdfCreator blankPoaCreator;
    private final CmrToXmlCreator xmlCreator;

    @FXML
    public AnchorPane cmrAnchorPane;
    @FXML
    public AnchorPane addCmrTabAnchorPane;
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
    public Button createPdfButton;
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
    public ComboBox<String> cbTerminal;
    @FXML
    public DatePicker dpCMRDate;
    @FXML
    public TabPane cmrTabPane;
    @FXML
    public Tab addCmrTab;
    @FXML
    public Tab searchResultTab;
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
    public TableColumn<CMR, String> listTerminal;
    @FXML
    public TextField truckLabel;
    @FXML
    public TextField trailerLabel;
    @FXML
    public TableView<CMR> searchResultTable;
    @FXML
    public TableColumn<CMR, String> searchNumber;
    @FXML
    public TableColumn<CMR, String> searchDate;
    @FXML
    public TableColumn<CMR, String> searchOrder;
    @FXML
    public TableColumn<CMR, String> searchSender;
    @FXML
    public TableColumn<CMR, String> searchRecipient;
    @FXML
    public TableColumn<CMR, String> searchPOD;
    @FXML
    public TableColumn<CMR, String> searchPOL;
    @FXML
    public TableColumn<CMR, String> searchDocuments;
    @FXML
    public TableColumn<CMR, String> searchContainer;
    @FXML
    public TableColumn<CMR, String> searchCargoName;
    @FXML
    public TableColumn<CMR, String> searchCargoQuantity;
    @FXML
    public TableColumn<CMR, String> searchCargoWeight;
    @FXML
    public TableColumn<CMR, String> searchCargoCode;
    @FXML
    public TableColumn<CMR, String> searchSenderInstructions;
    @FXML
    public TableColumn<CMR, String> searchDriver;
    @FXML
    public TableColumn<CMR, String> searchTruck;
    @FXML
    public TableColumn<CMR, String> searchTrailer;
    @FXML
    public TableColumn<CMR, String> searchTerminal;
    @FXML
    public DatePicker dpSearchByDate;
    @FXML
    public TextField tfSearchByContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSenderList.getItems().setAll(senderService.findAll().stream().map(Sender::getName).toList());
        new AutoCompleteComboBoxListener<>(cbSenderList);
        cbRecipientList.getItems().setAll(recipientService.findAll().stream().map(Recipient::getName).toList());
        new AutoCompleteComboBoxListener<>(cbRecipientList);
        cbPOD.getItems().setAll(podService.findAllPlaceOfDelivery().stream().map(PlaceOfDelivery::getAddress).toList());
        new AutoCompleteComboBoxListener<>(cbPOD);
        cbPOL.getItems().setAll(polService.findAllPlaceOfLoading().stream().map(PlaceOfLoading::getAddress).toList());
        new AutoCompleteComboBoxListener<>(cbPOL);
        cbContainerList.getItems().setAll(containerService.findAll().stream().map(Container::getNumber).toList());
        new AutoCompleteComboBoxListener<>(cbContainerList);
        cbDriverList.getItems().setAll(driverService.findAll().stream()
                .map(driver -> driver.getLastName() + " " + driver.getFirstName() + " " + driver.getMiddleName())
                .toList());
        new AutoCompleteComboBoxListener<>(cbDriverList);
        cbTerminal.getItems().setAll(terminalService.findAll().stream().map(Terminal::getName).toList());
        new AutoCompleteComboBoxListener<>(cbTerminal);
        cbSenderList.setOnShowing(e -> {
            cbSenderList.getItems().setAll(senderService.findAll().stream().map(Sender::getName).toList());
            new AutoCompleteComboBoxListener<>(cbSenderList);
        });
        cbRecipientList.setOnShowing(e -> {
            cbRecipientList.getItems().setAll(recipientService.findAll().stream().map(Recipient::getName).toList());
            new AutoCompleteComboBoxListener<>(cbRecipientList);
        });
        cbPOD.setOnShowing(e -> {
            cbPOD.getItems().setAll(podService.findAllPlaceOfDelivery().stream().map(PlaceOfDelivery::getAddress).toList());
            new AutoCompleteComboBoxListener<>(cbPOD);
        });
        cbPOL.setOnShowing(e -> {
            cbPOL.getItems().setAll(polService.findAllPlaceOfLoading().stream().map(PlaceOfLoading::getAddress).toList());
            new AutoCompleteComboBoxListener<>(cbPOL);
        });
        cbContainerList.setOnShowing(e -> {
            cbContainerList.getItems().setAll(containerService.findAll().stream().map(Container::getNumber).toList());
            new AutoCompleteComboBoxListener<>(cbContainerList);
        });
        cbDriverList.setOnShowing(e -> {
            cbDriverList.getItems().setAll(driverService.findAll().stream()
                    .map(driver -> driver.getLastName() + " " + driver.getFirstName() + " " + driver.getMiddleName())
                    .toList());
            new AutoCompleteComboBoxListener<>(cbDriverList);
        });
        cbTerminal.setOnShowing(e -> {
            cbTerminal.getItems().setAll(terminalService.findAll().stream().map(Terminal::getName).toList());
            new AutoCompleteComboBoxListener<>(cbTerminal);
        });
        getCmrListButton.setOnAction(event -> getCmrTableAction());
        tableContextMenuHandler(cmrListTable);
        tableContextMenuHandler(searchResultTable);
    }

    public void saveCmrAction() {
        if (cbSenderList.getValue() != null && cbRecipientList.getValue() != null &&
                cbPOL.getValue() != null && cbPOD.getValue() != null &&
                cbContainerList.getValue() != null && cbDriverList.getValue() != null) {
            try {
                var cmr = fillCmr();
                cmrService.save(cmr);
                Alerts.successAlert("CMR №" + cmr.getNumber() + " успешно сохранена в базу данных!");
                refresh();
            } catch (Exception e) {
                Alerts.errorAlert("Заполните обязательные поля!");
            }
        }
    }

    public Boolean updateCmrAction(Integer id) {
        try {
            var cmr = fillCmr();
            cmr.setId(id);
            cmrService.update(cmr);
            Alerts.successAlert("CMR №" + cmr.getNumber() + " успешно обновлена!");
            refresh();
            return true;
        } catch (Exception e) {
            Alerts.errorAlert("Заполните обязательные поля!");
            return false;
        }
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
                listTerminal,
                cmrListTable);
    }

    public void addNewSender() {
        mainWindowController.senderButtonAction();
    }

    public void addNewRecipient() {
        mainWindowController.recipientButtonAction();
    }

    public void addNewPOL() {
        mainWindowController.polButtonAction();
    }

    public void addNewPOD() {
        mainWindowController.podButtonAction();
    }

    public void addNewContainer() {
        mainWindowController.containerButtonAction();
    }

    public void addNewDriver() {
        mainWindowController.driverButtonAction();
    }

    public void addNewTerminal() {
        mainWindowController.terminalButtonAction();
    }

    public void dateSearchAction() {
        try {
            List<CMR> cmrByDate = cmrService.findByDate(dpSearchByDate.getValue());
            fillTableBySearchResult(cmrByDate,
                    searchNumber,
                    searchDate,
                    searchOrder,
                    searchSender,
                    searchRecipient,
                    searchPOD,
                    searchPOL,
                    searchDocuments,
                    searchContainer,
                    searchCargoName,
                    searchCargoQuantity,
                    searchCargoWeight,
                    searchCargoCode,
                    searchSenderInstructions,
                    searchDriver,
                    searchTruck,
                    searchTrailer,
                    searchTerminal,
                    searchResultTable);
            if (cmrByDate.isEmpty()) Alerts.errorAlert("Данные не найдены");
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Данные не найдены");
        }
    }

    public void containerSearchAction() {
        try {
            List<CMR> cmrByContainer = cmrService.findByContainerNumber(tfSearchByContainer.getText());
            fillTableBySearchResult(cmrByContainer,
                    searchNumber,
                    searchDate,
                    searchOrder,
                    searchSender,
                    searchRecipient,
                    searchPOD,
                    searchPOL,
                    searchDocuments,
                    searchContainer,
                    searchCargoName,
                    searchCargoQuantity,
                    searchCargoWeight,
                    searchCargoCode,
                    searchSenderInstructions,
                    searchDriver,
                    searchTruck,
                    searchTrailer,
                    searchTerminal,
                    searchResultTable);
            if (cmrByContainer.isEmpty()) Alerts.errorAlert("Данные не найдены");
        } catch (CmrEntityNotFoundException e) {
            Alerts.errorAlert("Данные не найдены");
        }
    }

    public void fillDriver() {
        if (!cbDriverList.getSelectionModel().isEmpty()) {
            try {
                var driver = driverService.findByFullName(cbDriverList.getValue());
                truckLabel.setText(driver.getTruck());
                trailerLabel.setText(driver.getTrailer());
                truckLabel.setEditable(false);
                trailerLabel.setEditable(false);
                truckLabel.setStyle("-fx-text-fill: #000000");
                trailerLabel.setStyle("-fx-text-fill: #000000");
            } catch (CmrEntityNotFoundException e) {
                Alerts.errorAlert("Данные не найдены");
            }
        }
    }

    public void closeButtonAction() {
        CmrPdfCleaner.cleanUpPdf();
        cmrAnchorPane.getScene().getWindow().hide();
    }

    public void viewPdfAction() {
        try {
            var cmr = fillCmr();
            viewPdf(cmr);
        } catch (Exception e) {
            Alerts.errorAlert("Не могу заполнить CMR. Проверьте, внесены ли все данные.");
        }
    }

    private void editCmrAction(CMR clickedRowCmr) {
        tfCMRNumber.setText(clickedRowCmr.getNumber());
        dpCMRDate.setValue(clickedRowCmr.getDate());
        tfOrderNumber.setText(clickedRowCmr.getOrderNumber());
        cbSenderList.setValue(clickedRowCmr.getSender() != null ? clickedRowCmr.getSender().getName() : null);
        cbRecipientList.setValue(clickedRowCmr.getRecipient() != null ? clickedRowCmr.getRecipient().getName() : null);
        cbPOL.setValue(clickedRowCmr.getPlaceOfLoading() != null ? clickedRowCmr.getPlaceOfLoading().getAddress() : null);
        cbPOD.setValue(clickedRowCmr.getPlaceOfDelivery() != null ? clickedRowCmr.getPlaceOfDelivery().getAddress() : null);
        taDocuments.setText(clickedRowCmr.getDocuments());
        taCargoName.setText(clickedRowCmr.getCargoName());
        cbContainerList.setValue(clickedRowCmr.getContainer() != null ? clickedRowCmr.getContainer().getNumber() : null);
        tfCargoQuantity.setText(clickedRowCmr.getCargoQuantity());
        tfCargoWeight.setText(clickedRowCmr.getCargoWeight());
        tfCargoCode.setText(clickedRowCmr.getCargoCode());
        taSenderInstructions.setText(clickedRowCmr.getSendersInstructions());
        tfIssuePlace.setText(clickedRowCmr.getPlaceOfIssue());
        cbDriverList.setValue(clickedRowCmr.getDriver() != null ? clickedRowCmr.getDriver().getLastName() + " " + clickedRowCmr.getDriver().getFirstName() + " " + clickedRowCmr.getDriver().getMiddleName() : null);
        cbTerminal.setValue((clickedRowCmr.getTerminal().getName()));
        saveCmrButton.setVisible(false);
        Button updateButton = new Button("Обновить");
        addCmrTabAnchorPane.getChildren().addAll(updateButton);
        updateButton.setId("custom-button");
        updateButton.setPrefHeight(30.0);
        updateButton.setPrefWidth(150.0);
        AnchorPane.setLeftAnchor(updateButton, 200.0);
        AnchorPane.setTopAnchor(updateButton, 540.0);
        updateButton.setOnAction(event -> {
            if (Boolean.TRUE.equals(updateCmrAction(clickedRowCmr.getId()))) {
                updateButton.setVisible(false);
                saveCmrButton.setVisible(true);
            }
        });
        cmrTabPane.getSelectionModel().select(addCmrTab);
    }

    private void tableContextMenuHandler(TableView<CMR> cmrListTable) {
        ContextMenu cm = new ContextMenu();
        MenuItem editMI = new MenuItem("Редактировать");
        MenuItem viewMI = new MenuItem("Просмотреть");
        Menu poaMI = new Menu("Доверенность");
        MenuItem poaBlankSubMI = new MenuItem("Бланк предприятия");
        MenuItem poaMd2SubMI = new MenuItem("Бланк МД-2");
        poaMI.getItems().addAll(poaBlankSubMI, poaMd2SubMI);
        MenuItem deleteMI = new MenuItem("Удалить");
        MenuItem toXmlMI = new MenuItem("Создать XML");
        cm.getItems().addAll(editMI, viewMI, poaMI, deleteMI, new SeparatorMenuItem(), toXmlMI);
        cmrListTable.setRowFactory(tv -> {
            TableRow<CMR> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(cmrListTable, t.getScreenX(), t.getScreenY());
                    CMR clickedRowCmr = row.getItem();
                    //edit cmr
                    editMI.setOnAction(edit -> editCmrAction(clickedRowCmr));
                    //create Power of Attorney MD-2
                    poaMd2SubMI.setOnAction(poa -> {
                        try {
                            viewPoaMD2(clickedRowCmr);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Alerts.errorAlert("Не могу заполнить Доверенность.\nПроверьте, внесены ли все данные.");
                        }
                    });
                    //create Power of Attorney on firm blank
                    poaBlankSubMI.setOnAction(poa -> {
                        try {
                            viewPoaBlank(clickedRowCmr);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Alerts.errorAlert("Не могу заполнить Доверенность.\nПроверьте, внесены ли все данные.");
                        }
                    });
                    //view row in PDF
                    viewMI.setOnAction(view -> {
                        try {
                            viewPdf(clickedRowCmr);
                        } catch (Exception e) {
                            Alerts.errorAlert("Не могу заполнить CMR.\nПроверьте, внесены ли все данные.");
                        }
                    });
                    //delete row
                    deleteMI.setOnAction(delete -> {
                        try {
                            cmrService.delete(clickedRowCmr);
                        } catch (CmrEntityNotFoundException e) {
                            Alerts.errorAlert("Не удалось удалить.");
                        }
                        getCmrTableAction();
                    });
                    //create XML
                    toXmlMI.setOnAction(xml -> {
                        try {
                            xmlCreator.createXML(clickedRowCmr);
                        } catch (ParserConfigurationException | IOException | TransformerException e) {
                            e.printStackTrace();
                            Alerts.errorAlert("Не удалось создать XML-файл");
                        }
                    });
                }
            });
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if (t.getButton() == MouseButton.PRIMARY && t.getClickCount() == 2) {
                    CMR clickedRowCmr = row.getItem();
                    editCmrAction(clickedRowCmr);
                }
            });
            return row;
        });
    }

    private void viewPoaBlank(CMR cmr) {
        String blankImage;
        try {
            Stage stage = (Stage) cmrAnchorPane.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            fileChooser.getExtensionFilters().add(extFilterJPG);
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                blankImage = selectedFile.getAbsolutePath();
                blankPoaCreator.createPoaPdfFile(cmr, blankImage);
                var file = new File("PowerOfAttorneyBlank" + cmr.getNumber() + ".pdf");
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.getAbsolutePath());
            } else {
                Alerts.errorAlert("Операция отменена.");
            }
        } catch (Exception e) {
            Alerts.errorAlert("Не удалось прочитать XML-файл.");
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

    private CMR fillCmr() throws CmrEntityNotFoundException {
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
        String cmrTerminal = cbTerminal.getValue();
        try {
            return CMR.builder()
                    .number(cmrNumber)
                    .date(cmrDate)
                    .orderNumber(cmrOrderNumber)
                    .sender(senderService.findByName(cmrSender))
                    .recipient(recipientService.findByName(cmrRecipient))
                    .placeOfDelivery(podService.findPlaceOfDeliveryByAddress(cmrPOD))
                    .placeOfLoading(polService.findPlaceOfLoadingByAddress(cmrPOL))
                    .documents(cmrDocuments)
                    .container(containerService.findByNumber(cmrContainer))
                    .cargoName(cmrCargoName)
                    .cargoQuantity(cmrCargoQuantity)
                    .cargoWeight(cmrCargoWeight)
                    .cargoCode(cmrCargoCode)
                    .sendersInstructions(cmrSendersInstructions)
                    .placeOfIssue(cmrIssuePlace)
                    .driver(driverService.findByFullName(cmrDriver))
                    .terminal(terminalService.findByName(cmrTerminal))
                    .build();
        } catch (Exception e) {
            throw new CmrEntityNotFoundException();
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
                                         TableColumn<CMR, String> terminal,
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
        terminal.setCellValueFactory(new PropertyValueFactory<>("terminal"));
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
        trailerLabel.clear();
        truckLabel.clear();
        cbTerminal.setValue(null);
    }
}
