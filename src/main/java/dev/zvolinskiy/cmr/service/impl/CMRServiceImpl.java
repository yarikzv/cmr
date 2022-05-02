package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.CMR;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.repo.CMRRepo;
import dev.zvolinskiy.cmr.service.CMRService;
import dev.zvolinskiy.cmr.service.ContainerService;
import dev.zvolinskiy.cmr.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CMRServiceImpl implements CMRService {

    private final CMRRepo cmrRepo;
    private final ContainerService containerService;
    private final RecipientService recipientService;

    @Override
    public CMR save(CMR cmr) {
        return cmrRepo.save(cmr);
    }

    @Override
    public CMR update(CMR cmr) {
        var updatedCMR = cmrRepo.findById(cmr.getId()).orElse(null);
        if (updatedCMR != null){
            updatedCMR.setNumber(cmr.getNumber());
            updatedCMR.setDate(cmr.getDate());
            updatedCMR.setOrderNumber(cmr.getOrderNumber());
            updatedCMR.setSender(cmr.getSender());
            updatedCMR.setRecipient(cmr.getRecipient());
            updatedCMR.setPlaceOfDelivery(cmr.getPlaceOfDelivery());
            updatedCMR.setPlaceOfLoading(cmr.getPlaceOfLoading());
            updatedCMR.setDocuments(cmr.getDocuments());
            updatedCMR.setContainer(cmr.getContainer());
            updatedCMR.setCargoQuantity(cmr.getCargoQuantity());
            updatedCMR.setCargoWeight(cmr.getCargoWeight());
            updatedCMR.setCargoCode(cmr.getCargoCode());
            updatedCMR.setCargoName(cmr.getCargoName());
            updatedCMR.setSendersInstructions(cmr.getSendersInstructions());
            updatedCMR.setPlaceOfIssue(cmr.getPlaceOfIssue());
            updatedCMR.setDriver(cmr.getDriver());
            updatedCMR.setTerminal(cmr.getTerminal());
            return cmrRepo.save(updatedCMR);
        } else {
            return cmr;
        }
    }

    @Override
    public CMR findById(Integer id) throws CmrEntityNotFoundException {
        var cmr = cmrRepo.findById(id).orElse(null);
        if (cmr != null) {
            return cmr;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<CMR> findByContainerNumber(String number) throws CmrEntityNotFoundException {
        var container = containerService.findByNumber(number);
        if (container != null) {
            return cmrRepo.findByContainer(container);
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<CMR> findByDate(LocalDate date) throws CmrEntityNotFoundException {
        var cmr = cmrRepo.findCMRSByDate(date);
        if (cmr != null) {
            return cmr;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<CMR> findByRecipientName(String recipientName) throws CmrEntityNotFoundException {
        var recipient = recipientService.findByName(recipientName);
        if (recipient != null) {
            var list = cmrRepo.findCMRSByRecipient(recipient);
            if (list != null) {
                return list;
            } else {
                throw new CmrEntityNotFoundException();
            }
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<CMR> findAll() {
        return (List<CMR>) cmrRepo.findAll();
    }

    @Override
    public void delete(CMR cmr) throws CmrEntityNotFoundException {
        try {
            cmrRepo.delete(cmr);
        } catch (IllegalArgumentException e) {
            throw new CmrEntityNotFoundException();
        }
    }
}
