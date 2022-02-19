package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Passport;
import dev.zvolinskiy.cmr.repo.PassportRepo;
import dev.zvolinskiy.cmr.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {

    private final PassportRepo passportRepo;

    @Override
    public Passport savePassport(Passport passport) {
        return passportRepo.save(passport);
    }

    @Override
    public Passport findPassportById(Integer id) {
        return passportRepo.findById(id).orElse(null);
    }

    @Override
    public Passport findPassportByNumber(String passportNumber) {
        return passportRepo.findPassportByNumber(passportNumber);
    }

    @Override
    public void deletePassport(Passport passport) {
        passportRepo.delete(passport);
    }
}
