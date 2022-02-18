package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Passport;
import org.springframework.data.repository.CrudRepository;

public interface PassportRepo extends CrudRepository<Passport, Integer> {
}
