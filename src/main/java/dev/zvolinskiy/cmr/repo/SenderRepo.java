package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Sender;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SenderRepo extends CrudRepository<Sender, Integer> {
    List<Sender> findSendersByNameContaining(String name);
}
