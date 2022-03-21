package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Recipient;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.repo.RecipientRepo;
import dev.zvolinskiy.cmr.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipientServiceImpl implements RecipientService {

    private final RecipientRepo recipientRepo;

    @Override
    public Recipient save(Recipient recipient) {
        return recipientRepo.save(recipient);
    }

    @Override
    public Recipient update(Recipient recipient) {
        Recipient updatedRecipient = recipientRepo.findById(recipient.getId()).orElse(null);
        if (updatedRecipient != null) {
            updatedRecipient.setEdrpou(recipient.getEdrpou());
            updatedRecipient.setName(recipient.getName());
            updatedRecipient.setAddress(recipient.getAddress());
            updatedRecipient.setCountry(recipient.getCountry());
            return recipientRepo.save(updatedRecipient);
        } else {
            return recipient;
        }
    }

    @Override
    public Recipient findById(Integer id) throws CmrEntityNotFoundException {
        var recipient = recipientRepo.findById(id).orElse(null);
        if (recipient != null) {
            return recipient;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public Recipient findByEdrpou(String code) throws CmrEntityNotFoundException {
        var recipient = recipientRepo.findByEdrpou(code);
        if (recipient != null) {
            return recipient;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public Recipient findByName(String name) throws CmrEntityNotFoundException {
        var recipient = recipientRepo.findRecipientsByNameContainingIgnoreCase(name);
        if (recipient != null) {
            return recipient;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<Recipient> findAll() {
        return (List<Recipient>) recipientRepo.findAll();
    }

    @Override
    public void delete(Recipient recipient) throws CmrEntityNotFoundException {
        try {
            recipientRepo.delete(recipient);
        } catch (IllegalArgumentException e) {
            throw new CmrEntityNotFoundException();
        }
    }
}
