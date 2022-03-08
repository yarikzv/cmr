package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.repo.ContainerRepo;
import dev.zvolinskiy.cmr.service.ContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContainerServiceImpl implements ContainerService {

    private final ContainerRepo containerRepo;

    @Override
    public Container saveContainer(Container container) {
        return containerRepo.save(container);
    }

    @Override
    public Container findContainerById(Integer id) {
        return containerRepo.findById(id).orElse(null);
    }

    @Override
    public Container findContainerByNumber(String number) {
        return containerRepo.findContainerByNumber(number);
    }

    @Override
    public List<Container> findAllContainers() {
        return (List<Container>) containerRepo.findAll();
    }

    @Override
    public void deleteContainer(Container container) {
        containerRepo.delete(container);
    }
}
