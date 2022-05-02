package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Terminal;
import org.springframework.data.repository.CrudRepository;

public interface TerminalRepo extends CrudRepository<Terminal, Integer> {
    Terminal findByNameContainingIgnoreCase(String name);
}
