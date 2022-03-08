package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.entity.*;
import dev.zvolinskiy.cmr.service.*;
import dev.zvolinskiy.cmr.util.AutoCompleteComboBoxListener;
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

    public AnchorPane cmrAnchorPane;
    public Button bNewSender;
    public Button bNewRecipient;
    public Button bNewPOL;
    public Button bNewPOD;
    public Button bNewContainer;
    public Button bNewDriver;
    public Button saveCmrButton;
    public ComboBox<String> cbSenderList;
    public ComboBox<String> cbRecipientList;
    public ComboBox<String> cbPOD;
    public ComboBox<String> cbPOL;
    public ComboBox<String> cbContainerList;
    public ComboBox<String> cbDriverList;
    public DatePicker dpCMRDate;
    public Tab addCmrTab;
    public TabPane cmrTabPane;
    public TextArea taDocuments;
    public TextArea taCargoName;
    public TextArea taSenderInstructions;
    public TextField tfOrderNumber;
    public TextField tfCargoQuantity;
    public TextField tfCMRNumber;
    public TextField tfCargoWeight;
    public TextField tfCargoCode;
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
                .driver(driverService.findDriverByPassport(cmrDriver))
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
    }

    public void addNewRecipient() {
    }

    public void addNewPOL() {
    }

    public void addNewPOD() {
    }

    public void addNewContainer() {
    }

    public void addNewDriver() {
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
