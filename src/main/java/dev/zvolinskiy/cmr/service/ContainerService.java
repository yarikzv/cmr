package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Container;

import java.util.List;

public interface ContainerService {

    Container saveContainer(Container container);

    Container findContainerById(Integer id);

    Container findContainerByNumber(String number);

    List<Container> findAllContainers();

    void deleteContainer(Container container);
}
