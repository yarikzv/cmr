package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.CMR;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface CMRService {

    CMR save(CMR cmr);

    CMR update(CMR cmr);

    CMR findById(Integer id) throws CmrEntityNotFoundException;

    List<CMR> findByContainerNumber(String number) throws CmrEntityNotFoundException;

    List<CMR> findByDate(LocalDate date) throws CmrEntityNotFoundException;

    List<CMR> findByRecipientName(String recipientName) throws CmrEntityNotFoundException;

    List<CMR> findAll();

    void delete(CMR cmr) throws CmrEntityNotFoundException;
}
