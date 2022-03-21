package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;

import java.util.List;

public interface ContainerService {

    Container save(Container container);

    Container findById(Integer id) throws CmrEntityNotFoundException;

    Container findByNumber(String number) throws CmrEntityNotFoundException;

    List<Container> findAll();

    void delete(Container container) throws CmrEntityNotFoundException;

    Container update(Container container);
}
