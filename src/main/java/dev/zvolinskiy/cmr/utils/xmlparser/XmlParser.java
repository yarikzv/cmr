package dev.zvolinskiy.cmr.utils.xmlparser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class XmlParser {
    private final Converter converter;

    public void parser(File file) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        XmlCmr xmlCmr = new XmlCmr();

        Element orderNumberElement = (Element) doc.getElementsByTagName("OWN_NUM").item(0);
        Element orderDateElement = (Element) doc.getElementsByTagName("OWN_DATE").item(0);
        Element documentsElement = (Element) doc.getElementsByTagName("BILL_NUM").item(0);
        Element recipientEdrpouElement = (Element) doc.getElementsByTagName("CSG_EDRPO").item(0);
        Element recipientAddressElement = (Element) doc.getElementsByTagName("CSG_ADR").item(0);
        Element recipientNameElement = (Element) doc.getElementsByTagName("CSG_NAME").item(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        xmlCmr.setDate(LocalDate.parse(orderDateElement.getTextContent(), formatter));
        xmlCmr.setOrder(orderNumberConverter(orderNumberElement.getTextContent(), orderDateElement.getTextContent()));
        xmlCmr.setDocuments("к/с: " + documentsElement.getTextContent());
        xmlCmr.setRecipientEdrpou(recipientEdrpouElement.getTextContent());
        xmlCmr.setRecipientAddress(recipientAddressElement.getTextContent());
        xmlCmr.setRecipientName(recipientNameElement.getTextContent());

        NodeList cmrNodeList = doc.getElementsByTagName("CONTAINER");
        List<XmlContainer> xmlContList = new ArrayList<>();
        for (int i = 0; i < cmrNodeList.getLength(); i++) {
            Element containerElement = (Element) cmrNodeList.item(i);
            XmlContainer xmlContainer = new XmlContainer();
            if (cmrNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                NodeList childNodes = containerElement.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element) childNodes.item(j);
                        switch (childElement.getNodeName()) {
                            case "CONTAINER_NUM" -> xmlContainer.setContNumber(childElement.getTextContent());
                            case "CONTAINER_SIZE" -> xmlContainer.setContType(childElement.getTextContent());
                        }
                        NodeList cargoNodeList = doc.getElementsByTagName("CARGO");
                        for (int k = 0; k < cargoNodeList.getLength(); k++) {
                            Element cargoElement = (Element) cargoNodeList.item(k);
                            List<XmlCargo> cargoList = new ArrayList<>();
                            if (cargoNodeList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                NodeList cargoChildNodes = cargoElement.getChildNodes();
                                XmlCargo cargo = new XmlCargo();
                                for (int l = 0; l < cargoChildNodes.getLength(); l++) {
                                    if (cargoChildNodes.item(l).getNodeType() == Node.ELEMENT_NODE) {
                                        Element cargoChildElement = (Element) cargoChildNodes.item(l);
                                        switch (cargoChildElement.getNodeName()) {
                                            case "GOODS_DESCR" -> cargo.setCargoName(cargoChildElement.getTextContent());
                                            case "PIECES" -> cargo.setCargoQuantity(cargoChildElement.getTextContent());
                                            case "GROSS" -> cargo.setCargoWeight(cargoChildElement.getTextContent());
                                            case "GOODS_CODE" -> cargo.setCargoCode(cargoChildElement.getTextContent());
                                        }
                                        cargoList.add(cargo);
                                    }
                                }
                            }
                            xmlContainer.setCargoList(cargoList);
                        }
                    }
                }
            }
            xmlContList.add(xmlContainer);
        }
        xmlCmr.setContainerList(xmlContList);
        converter.toCmrConverter(xmlCmr);
    }

    private String orderNumberConverter(String orderNumber, String orderDate){
        String day = orderDate.substring(6,8);
        String month = orderDate.substring(4,6);
        String year = orderDate.substring(0,4 );
        return "№" + orderNumber + " від " + day + "." + month + "." + year;
    }
}
