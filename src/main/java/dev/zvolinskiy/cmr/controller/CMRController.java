package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.*;
import dev.zvolinskiy.cmr.service.*;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
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
    public Tab addCmrTab;
    @FXML
    public TabPane cmrTabPane;
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


    public void saveCmrAction() {
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

        CMR cmr = CMR.builder()
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
                .cargoQuantity(Integer.valueOf(cmrCargoQuantity))
                .cargoWeight(cmrCargoWeight)
                .cargoCode(Long.valueOf(cmrCargoCode))
                .sendersInstructions(cmrSendersInstructions)
                .placeOfIssue(cmrIssuePlace)
                .driver(driverService.findDriverByFullName(cmrDriver))
                .build();

        cmrService.saveCMR(cmr);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "CMR №" +
                        cmr.getNumber() +
                        " успешно сохранена в базу данных!",
                ButtonType.OK);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) alert.close();
        });
        refresh();
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
    }

    public void getCmrListAction() {
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
    }

    public void closeButtonAction() {
        cmrAnchorPane.getScene().getWindow().hide();
    }
}
