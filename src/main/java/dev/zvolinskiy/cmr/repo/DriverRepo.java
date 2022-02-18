package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Driver;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepo extends CrudRepository<Driver, Integer> {
}
