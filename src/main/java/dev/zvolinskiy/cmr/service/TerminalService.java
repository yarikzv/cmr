package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Terminal;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;

import java.util.List;

public interface TerminalService {

    Terminal save(Terminal terminal);

    Terminal update(Terminal terminal);

    Terminal findById(Integer id) throws CmrEntityNotFoundException;

    Terminal findByName(String name) throws CmrEntityNotFoundException;

    List<Terminal> findAll();

    void delete(Terminal terminal) throws CmrEntityNotFoundException;
}
