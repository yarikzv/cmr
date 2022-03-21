package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Recipient;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;

import java.util.List;

public interface RecipientService {

    Recipient save(Recipient recipient);

    Recipient update(Recipient recipient);

    Recipient findById(Integer id) throws CmrEntityNotFoundException;

    Recipient findByEdrpou(String code) throws CmrEntityNotFoundException;

    Recipient findByName(String name) throws CmrEntityNotFoundException;

    List<Recipient> findAll();

    void delete(Recipient recipient) throws CmrEntityNotFoundException;
}
