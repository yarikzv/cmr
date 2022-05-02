package dev.zvolinskiy.cmr.utils.xmlparser;

import dev.zvolinskiy.cmr.entity.*;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.service.*;
import dev.zvolinskiy.cmr.utils.Alerts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Converter {
    private final RecipientService recipientService;
    private final CountryService countryService;
    private final ContainerService containerService;
    private final TerminalService terminalService;
    private final CMRService cmrService;

    public void toCmrConverter(XmlCmr xmlCmr) {
        int quantity = xmlCmr.getContainerList().size();
        for (int i = 0; i < quantity; i++) {
            try {
                cmrService.findByContainerNumber(xmlCmr.getContainerList().get(i).getContNumber());
                Alerts.successAlert((i + 1) + "-я CMR существует в базе. Воспользуйтесь поиском.");
            } catch (CmrEntityNotFoundException e) {
                cmrService.save(CMR.builder()
                        .number("")
                        .date(xmlCmr.getDate())
                        .orderNumber(xmlCmr.getOrder())
                        .documents(xmlCmr.getDocuments())
                        .recipient(recipientFromXml(xmlCmr))
                        .container(containerFromXml(xmlCmr.getContainerList().get(i)))
                        .cargoName(xmlCmr.getContainerList().get(i).getCargoList().get(0).getCargoName())
                        .cargoQuantity(xmlCmr.getContainerList().get(i).getCargoList().get(0).getCargoQuantity())
                        .cargoWeight(xmlCmr.getContainerList().get(i).getCargoList().get(0).getCargoWeight())
                        .cargoCode(xmlCmr.getContainerList().get(i).getCargoList().get(0).getCargoCode())
                        .placeOfIssue("")
                        .sendersInstructions("")
                        .terminal(terminalFromXml(xmlCmr))
                        .build());
                Alerts.successAlert((i + 1) + "-я CMR успешно сохранена в базу данных.\n" +
                        "Для редактирования воспользуйтесь поиском.");
            }
        }
    }

    private Terminal terminalFromXml(XmlCmr xmlCmr) {
        try {
            return terminalService.findByName(xmlCmr.getTerminal());
        } catch (CmrEntityNotFoundException e) {
            return terminalService.save(Terminal.builder()
                    .name(xmlCmr.getTerminal())
                    .build());
        }
    }

    private Container containerFromXml(XmlContainer xmlContainer) {
        try {
            return containerService.findByNumber(xmlContainer.getContNumber());
        } catch (CmrEntityNotFoundException e) {
            return containerService.save(Container.builder()
                    .number(xmlContainer.getContNumber())
                    .type(xmlContainer.getContType())
                    .build()
            );
        }
    }

    private Recipient recipientFromXml(XmlCmr xmlCmr) {
        String[] addressArray = xmlCmr.getRecipientAddress().split(", ");
        String country = addressArray[0];
        String address = Arrays.stream(addressArray).skip(1).collect(Collectors.joining(", "));
        Country recipientCountry = countryService.findCountryByName(country);
        try {
            return recipientService.findByEdrpou(xmlCmr.getRecipientEdrpou());
        } catch (CmrEntityNotFoundException e) {
            return recipientService.save(Recipient.builder()
                    .edrpou(xmlCmr.getRecipientEdrpou())
                    .name(xmlCmr.getRecipientName())
                    .address(address)
                    .country(recipientCountry)
                    .build());
        }
    }
}
