package dev.zvolinskiy.cmr.utils.xmlparser;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
class XmlCmr {
    private LocalDate date;
    private String order;
    private String documents;
    private String recipientEdrpou;
    private String recipientAddress;
    private String recipientName;
    private List<XmlContainer> containerList;
    private String terminal;
}
