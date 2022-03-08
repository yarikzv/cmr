package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Recipient;

import java.util.List;

public interface RecipientService {

    Recipient saveRecipient(Recipient recipient);

    Recipient findRecipientById(Integer id);

    Recipient findRecipientByName(String name);

    List<Recipient> findAllRecipient();

    void deleteRecipient(Recipient recipient);
}
