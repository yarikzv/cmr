package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Sender;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.repo.SenderRepo;
import dev.zvolinskiy.cmr.service.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SenderServiceImpl implements SenderService {

    private final SenderRepo senderRepo;

    @Override
    public Sender save(Sender sender) {
        return senderRepo.save(sender);
    }

    @Override
    public Sender update(Sender sender) {
        var updatedSender = senderRepo.findById(sender.getId()).orElse(null);
        if (updatedSender != null) {
            updatedSender.setName(sender.getName());
            updatedSender.setAddress(sender.getAddress());
            updatedSender.setCountry(sender.getCountry());
            return senderRepo.save(updatedSender);
        } else {
            return sender;
        }
    }

    @Override
    public Sender findById(Integer id) throws CmrEntityNotFoundException {
        var sender = senderRepo.findById(id).orElse(null);
        if (sender != null) {
            return sender;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public Sender findByName(String name) throws CmrEntityNotFoundException {
        var sender = senderRepo.findSendersByNameContainingIgnoreCase(name);
        if (sender != null) {
            return sender;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<Sender> findAll() {
        return (List<Sender>) senderRepo.findAll();
    }


    @Override
    public void delete(Sender sender) throws CmrEntityNotFoundException {
        try {
            senderRepo.delete(sender);
        } catch (IllegalArgumentException e){
            throw new CmrEntityNotFoundException();
        }
    }
}
