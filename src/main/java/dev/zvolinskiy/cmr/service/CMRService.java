package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.CMR;

import java.sql.Date;
import java.util.List;

public interface CMRService {

    CMR saveCMR(CMR cmr);

    CMR findCMRById(Integer id);

    List<CMR> findCMRByContainerNumber(String number);

    List<CMR> findCMRByDate(Date date);

    List<CMR> findCMRByRecipientName(String recipientName);

    List<CMR> findAllCMRs();

    void deleteCMR(CMR cmr);
}
