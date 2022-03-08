package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Recipient;
import org.springframework.data.repository.CrudRepository;

public interface RecipientRepo extends CrudRepository<Recipient, Integer> {
    Recipient findRecipientsByName(String name);
}
