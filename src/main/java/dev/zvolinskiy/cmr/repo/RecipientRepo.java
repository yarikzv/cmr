package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Recipient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipientRepo extends CrudRepository<Recipient, Integer> {
    List<Recipient> findRecipientsByNameContaining(String name);
}
