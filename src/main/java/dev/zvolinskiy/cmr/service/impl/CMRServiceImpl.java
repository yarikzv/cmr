package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.CMR;
import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.entity.Recipient;
import dev.zvolinskiy.cmr.repo.CMRRepo;
import dev.zvolinskiy.cmr.service.CMRService;
import dev.zvolinskiy.cmr.service.ContainerService;
import dev.zvolinskiy.cmr.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CMRServiceImpl implements CMRService {

    private final CMRRepo cmrRepo;
    private final ContainerService containerService;
    private final RecipientService recipientService;

    @Override
    public CMR saveCMR(CMR cmr) {
        return cmrRepo.save(cmr);
    }

    @Override
    public CMR findCMRById(Integer id) {
        return cmrRepo.findById(id).orElse(null);
    }

    @Override
    public List<CMR> findCMRByContainerNumber(String number) {
        Container container = containerService.findContainerByNumber(number);
        if (container != null) {
            return cmrRepo.findCMRSByContainerContaining(container);
        } else {
            return null;
        }
    }

    @Override
    public List<CMR> findCMRByDate(Date date) {
        return cmrRepo.findCMRSByDate(date);
    }

    @Override
    public List<CMR> findCMRByRecipientName(String recipientName) {
        Recipient recipient = recipientService.findRecipientByName(recipientName);
        if (recipient != null) {
            return cmrRepo.findCMRSByRecipient(recipient);
        } else {
            return  null;
        }
    }

    @Override
    public List<CMR> findAllCMRs() {
        return (List<CMR>) cmrRepo.findAll();
    }

    @Override
    public void deleteCMR(CMR cmr) {
        cmrRepo.delete(cmr);
    }
}
