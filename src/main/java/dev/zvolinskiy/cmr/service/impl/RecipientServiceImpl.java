package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Recipient;
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
    public Recipient saveRecipient(Recipient recipient) {
        return recipientRepo.save(recipient);
    }

    @Override
    public Recipient findRecipientById(Integer id) {
        return recipientRepo.findById(id).orElse(null);
    }

    @Override
    public List<Recipient> findRecipientByName(String name) {
        return recipientRepo.findRecipientsByNameContaining(name);
    }

    @Override
    public List<Recipient> findAllRecipient() {
        return (List<Recipient>) recipientRepo.findAll();
    }

    @Override
    public void deleteRecipient(Recipient recipient) {
        recipientRepo.delete(recipient);
    }
}
