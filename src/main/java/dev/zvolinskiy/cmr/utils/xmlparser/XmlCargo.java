package dev.zvolinskiy.cmr.utils.xmlparser;

import lombok.Data;

@Data
class XmlCargo {
    private String cargoName;
    private String cargoQuantity;
    private String cargoWeight;
    private String cargoCode;
}
