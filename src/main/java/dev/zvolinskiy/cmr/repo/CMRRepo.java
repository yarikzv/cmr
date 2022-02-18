package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.CMR;
import org.springframework.data.repository.CrudRepository;

public interface CMRRepo extends CrudRepository<CMR, Integer> {
}
