package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Sender;
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
    public Sender saveSender(Sender sender) {
        return senderRepo.save(sender);
    }

    @Override
    public Sender findSenderById(Integer id) {
        return senderRepo.findById(id).orElse(null);
    }

    @Override
    public Sender findSenderByName(String name) {
        return senderRepo.findSendersByName(name);
    }

    @Override
    public List<Sender> findAllSenders() {
        return (List<Sender>) senderRepo.findAll();
    }


    @Override
    public void deleteSender(Sender sender) {
        senderRepo.delete(sender);
    }
}
