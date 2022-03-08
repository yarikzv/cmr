package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Container;
import org.springframework.data.repository.CrudRepository;

public interface ContainerRepo extends CrudRepository<Container, Integer> {
    Container findContainerByNumber(String number);
}
