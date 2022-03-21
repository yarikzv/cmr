package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Sender;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;

import java.util.List;

public interface SenderService {

    Sender save(Sender sender);

    Sender update(Sender sender);

    Sender findById(Integer id) throws CmrEntityNotFoundException;

    Sender findByName(String name) throws CmrEntityNotFoundException;

    List<Sender> findAll();

    void delete(Sender sender) throws CmrEntityNotFoundException;
}
