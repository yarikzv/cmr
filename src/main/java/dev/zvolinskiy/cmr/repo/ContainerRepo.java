package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Container;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContainerRepo extends CrudRepository<Container, Integer> {
    List<Container> findContainersByNumberContaining(String number);
}
