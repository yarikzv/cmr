package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Terminal;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.repo.TerminalRepo;
import dev.zvolinskiy.cmr.service.TerminalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {

    private final TerminalRepo terminalRepo;

    @Override
    public Terminal save(Terminal terminal) {
        return terminalRepo.save(terminal);
    }

    @Override
    public Terminal update(Terminal terminal) {
        Terminal updatedTerminal = terminalRepo.findById(terminal.getId()).orElse(null);
        if (updatedTerminal != null) {
            updatedTerminal.setName(terminal.getName());
            return terminalRepo.save(updatedTerminal);
        } else {
            return terminal;
        }
    }

    @Override
    public Terminal findById(Integer id) throws CmrEntityNotFoundException {
        var terminal = terminalRepo.findById(id).orElse(null);
        if (terminal != null) {
            return terminal;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public Terminal findByName(String number) throws CmrEntityNotFoundException {
        var terminal = terminalRepo.findByNameContainingIgnoreCase(number);
        if (terminal != null) {
            return terminal;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<Terminal> findAll() {
        return (List<Terminal>) terminalRepo.findAll();
    }

    @Override
    public void delete(Terminal terminal) throws CmrEntityNotFoundException {
        try {
            terminalRepo.delete(terminal);
        } catch (IllegalArgumentException e) {
            throw new CmrEntityNotFoundException();
        }
    }
}
