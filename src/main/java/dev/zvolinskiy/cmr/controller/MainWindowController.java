package dev.zvolinskiy.cmr.controller;

import dev.zvolinskiy.cmr.MainApplication;
import dev.zvolinskiy.cmr.utils.Alerts;
import dev.zvolinskiy.cmr.utils.pdf.CmrPdfCleaner;
import dev.zvolinskiy.cmr.utils.xmlparser.CmrToXmlCreator;
import dev.zvolinskiy.cmr.utils.xmlparser.XmlToCmrParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class MainWindowController implements Initializable {

    private final Resource cmrFxml;
    private final Resource driverFxml;
    private final Resource senderFxml;
    private final Resource recipientFxml;
    private final Resource podFxml;
    private final Resource polFxml;
    private final Resource containerFxml;
    private final Resource poaFxml;
    private final Resource terminalFxml;
    private final ApplicationContext applicationContext;
    private final XmlToCmrParser xmlParser;
    private final CmrToXmlCreator xmlCreator;

    @FXML
    public Button cmrButton;
    @FXML
    public Button driverButton;
    @FXML
    public Button senderButton;
    @FXML
    public Button recipientButton;
    @FXML
    public Button placeOfDeliveryButton;
    @FXML
    public Button placeOfLoadingButton;
    @FXML
    public Button containerButton;
    @FXML
    public Button closeButton;
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    public Button createFromXmlButton;
    @FXML
    public Button firmPOAButton;
    @FXML
    public Button createXmlFileButton;

    public MainWindowController(
            @Value("classpath:/fx/cmr.fxml") Resource cmrFxml,
            @Value("classpath:/fx/driver.fxml") Resource driverFxml,
            @Value("classpath:/fx/sender.fxml") Resource senderFxml,
            @Value("classpath:/fx/recipient.fxml") Resource recipientFxml,
            @Value("classpath:/fx/pod.fxml") Resource podFxml,
            @Value("classpath:/fx/pol.fxml") Resource polFxml,
            @Value("classpath:/fx/container.fxml") Resource containerFxml,
            @Value("classpath:/fx/poa.fxml") Resource poaFxml,
            @Value("classpath:/fx/terminal.fxml") Resource terminalFxml,
            ApplicationContext applicationContext, XmlToCmrParser xmlParser, CmrToXmlCreator xmlCreator) {
        this.cmrFxml = cmrFxml;
        this.driverFxml = driverFxml;
        this.senderFxml = senderFxml;
        this.recipientFxml = recipientFxml;
        this.podFxml = podFxml;
        this.polFxml = polFxml;
        this.containerFxml = containerFxml;
        this.poaFxml = poaFxml;
        this.terminalFxml = terminalFxml;
        this.applicationContext = applicationContext;
        this.xmlParser = xmlParser;
        this.xmlCreator = xmlCreator;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmrButton.setOnAction(event -> cmrButtonAction());
        driverButton.setOnAction(event -> driverButtonAction());
        senderButton.setOnAction(event -> senderButtonAction());
        recipientButton.setOnAction(event -> recipientButtonAction());
        placeOfDeliveryButton.setOnAction(event -> podButtonAction());
        placeOfLoadingButton.setOnAction(event -> polButtonAction());
        containerButton.setOnAction(event -> containerButtonAction());
        firmPOAButton.setOnAction(event -> firmPOAButtonAction());
        xmlButtonCreating();
        poaButtonCreating();
    }

    public void closeButtonAction() {
        CmrPdfCleaner.cleanUpPdf();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void cmrButtonAction() {
        customSceneLoader(cmrFxml);
    }

    public void driverButtonAction() {
        customSceneLoader(driverFxml);
    }

    public void senderButtonAction() {
        customSceneLoader(senderFxml);
    }

    public void recipientButtonAction() {
        customSceneLoader(recipientFxml);
    }

    public void podButtonAction() {
        customSceneLoader(podFxml);
    }

    public void polButtonAction() {
        customSceneLoader(polFxml);
    }

    public void containerButtonAction() {
        customSceneLoader(containerFxml);
    }

    public void firmPOAButtonAction() {
        customSceneLoader(poaFxml);
    }

    public void terminalButtonAction() {
        customSceneLoader(terminalFxml);
    }

    public void createFromXmlAction() {
        try {
            Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                xmlParser.parser(selectedFile);
                FXMLLoader fxmlLoader = customSceneLoader(cmrFxml);
                CMRController controller = Objects.requireNonNull(fxmlLoader).getController();
                controller.cmrTabPane.getSelectionModel().select(controller.tableCmrTab);
                controller.getCmrTableAction();
            } else {
                Alerts.errorAlert("Операция отменена.");
            }
        } catch (Exception e) {
            Alerts.errorAlert("Не удалось прочитать XML-файл.");
        }
    }

    private FXMLLoader customSceneLoader(Resource resourceFxml) {
        try {
            URL url = resourceFxml.getURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setControllerFactory(applicationContext::getBean);
            rootAnchorPane = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(rootAnchorPane);
            stage.setScene(scene);
            stage.setWidth(1000);
            stage.setHeight(700);
            stage.getIcons().add(new Image("icon.png"));
            stage.show();
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void xmlButtonCreating() {
        Image img = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/fx/images/xml2.png")));
        ImageView view = new ImageView(img);
        view.setFitHeight(40);
        view.setPreserveRatio(true);
        createFromXmlButton.setGraphic(view);
    }

    private void poaButtonCreating() {
        Image img = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/fx/images/poa.png")));
        ImageView view = new ImageView(img);
        view.setFitHeight(40);
        view.setPreserveRatio(true);
        firmPOAButton.setGraphic(view);
    }

    public void createXmlFileAction() {
    }
}
