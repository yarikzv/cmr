package dev.zvolinskiy.cmr.utils.xmlparser;

import dev.zvolinskiy.cmr.entity.CMR;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CmrToXmlCreator {

    public void createXML(CMR cmr) throws ParserConfigurationException, IOException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element rootElement = doc.createElement("DOCUMENT");
        doc.appendChild(rootElement);
        Element headerElement = doc.createElement("HEADER");
        rootElement.appendChild(headerElement);
        Element dateElement = doc.createElement("OWN_DATE");
        headerElement.appendChild(dateElement);
        dateElement.setTextContent(cmr.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Element bodyElement = doc.createElement("BODY");
        rootElement.appendChild(bodyElement);
        Element terminalElement = doc.createElement("TRM_CODE");
        bodyElement.appendChild(terminalElement);
        switch (cmr.getTerminal().getName()) {
            case "ДП \"КТО\"" -> terminalElement.setTextContent("UAODSCT");
            case "ТОВ \"БРУКЛІН-КИЇВ ПОРТ\"" -> terminalElement.setTextContent("UAODSBKP");
        }
        Element transportsElement = doc.createElement("TRANSPORTS");
        bodyElement.appendChild(transportsElement);
        Element transportElement = doc.createElement("TRANSPORT");
        transportsElement.appendChild(transportElement);
        Element billsElement = doc.createElement("BILLS");
        transportElement.appendChild(billsElement);
        Element billElement = doc.createElement("BILL");
        billsElement.appendChild(billElement);
        Element countryElement = doc.createElement("CSG_COUNTRY");
        billElement.appendChild(countryElement);
        countryElement.setTextContent(cmr.getRecipient().getCountry().getIso());
        Element addressElement = doc.createElement("CSG_ADR");
        billElement.appendChild(addressElement);
        addressElement.setTextContent(
                cmr.getRecipient().getCountry().getName() +
                        ", " +
                        cmr.getRecipient().getAddress());
        Element edrpouElement = doc.createElement("CSG_EDRPO");
        billElement.appendChild(edrpouElement);
        edrpouElement.setTextContent(cmr.getRecipient().getEdrpou());
        Element nameElement = doc.createElement("CSG_NAME");
        billElement.appendChild(nameElement);
        nameElement.setTextContent(cmr.getRecipient().getName());
        Element containersElement = doc.createElement("CONTAINERS");
        bodyElement.appendChild(containersElement);
        Element containerElement = doc.createElement("CONTAINER");
        containersElement.appendChild(containerElement);
        Element containerNumberElement = doc.createElement("CONTAINER_NUM");
        containerElement.appendChild(containerNumberElement);
        containerNumberElement.setTextContent(cmr.getContainer().getNumber());
        Element efElement = doc.createElement("EF");
        containerElement.appendChild(efElement);
        efElement.setTextContent("F");
        Element containerSizeElement = doc.createElement("CONTAINER_SIZE");
        containerElement.appendChild(containerSizeElement);
        containerSizeElement.setTextContent(cmr.getContainer().getType());
        Element cargoesElement = doc.createElement("CARGOES");
        containerElement.appendChild(cargoesElement);
        Element cargoElement = doc.createElement("CARGO");
        cargoesElement.appendChild(cargoElement);
        Element cargoCodeElement = doc.createElement("GOODS_CODE");
        cargoElement.appendChild(cargoCodeElement);
        cargoCodeElement.setTextContent(cmr.getCargoCode());
        Element grossElement = doc.createElement("GROSS");
        cargoElement.appendChild(grossElement);
        grossElement.setTextContent(cmr.getCargoWeight());
        Element piecesElement = doc.createElement("PIECES");
        cargoElement.appendChild(piecesElement);
        piecesElement.setTextContent(cmr.getCargoQuantity());
        Element goodsDescrElement = doc.createElement("GOODS_DESCR");
        cargoElement.appendChild(goodsDescrElement);
        goodsDescrElement.setTextContent(cmr.getCargoName());

        writeXml(doc, cmr);

    }

    private void writeXml(Document doc, CMR cmr) throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "windows-1251");
        DOMSource source = new DOMSource(doc);
        FileWriter writer = new FileWriter("cmr" + cmr.getNumber() + ".xml");
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
    }
}
