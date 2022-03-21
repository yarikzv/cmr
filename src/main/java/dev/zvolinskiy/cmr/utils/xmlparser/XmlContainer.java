package dev.zvolinskiy.cmr.utils.xmlparser;

import lombok.Data;

import java.util.List;

@Data
class XmlContainer {
    private String contNumber;
    private String contType;
    private List<XmlCargo> cargoList;
}
