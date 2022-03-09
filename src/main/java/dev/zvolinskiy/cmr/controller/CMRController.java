package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.*;
import dev.zvolinskiy.cmr.service.*;
import dev.zvolinskiy.cmr.util.AutoCompleteComboBoxListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
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


    public void saveCmrAction() {
        String cmrNumber = tfCMRNumber.getText();
        LocalDate cmrDate = dpCMRDate.getValue();
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
    }

    public void getCmrListAction() {
//        List<CMR> cmrList = cmrService.findAllCMRs();
//        fillTableBySearchResult(cmrList,
//                listNumber,
//                listType,
//                containerListTable);
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
}
