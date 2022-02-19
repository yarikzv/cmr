package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Sender;

import java.util.List;

public interface SenderService {

    Sender saveSender(Sender sender);

    Sender findSenderById(Integer id);

    List<Sender> findSenderByName(String name);

    List<Sender> findAllSenders();

    void deleteSender(Sender sender);
}
