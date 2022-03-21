package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
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
    public Container save(Container container) {
        return containerRepo.save(container);
    }

    @Override
    public Container update(Container container) {
        Container updatedContainer = containerRepo.findById(container.getId()).orElse(null);
        if (updatedContainer != null) {
            updatedContainer.setNumber(container.getNumber());
            updatedContainer.setType(container.getType());
            return containerRepo.save(updatedContainer);
        } else {
            return container;
        }
    }

    @Override
    public Container findById(Integer id) throws CmrEntityNotFoundException {
        var container = containerRepo.findById(id).orElse(null);
        if (container != null) {
            return container;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public Container findByNumber(String number) throws CmrEntityNotFoundException {
        var container = containerRepo.findByNumberContaining(number);
        if (container != null) {
            return container;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<Container> findAll() {
        return (List<Container>) containerRepo.findAll();
    }

    @Override
    public void delete(Container container) throws CmrEntityNotFoundException {
        try {
            containerRepo.delete(container);
        } catch (IllegalArgumentException e) {
            throw new CmrEntityNotFoundException();
        }
    }
}
