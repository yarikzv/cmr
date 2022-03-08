package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Sender;
import org.springframework.data.repository.CrudRepository;

public interface SenderRepo extends CrudRepository<Sender, Integer> {
    Sender findSendersByName(String name);
}
