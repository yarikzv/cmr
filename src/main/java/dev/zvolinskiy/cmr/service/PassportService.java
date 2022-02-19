package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Passport;

public interface PassportService {

    Passport savePassport(Passport passport);

    Passport findPassportById(Integer id);

    Passport findPassportByNumber(String passportNumber);

    void deletePassport(Passport passport);
}
