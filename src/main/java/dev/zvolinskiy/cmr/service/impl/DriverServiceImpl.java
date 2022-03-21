package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Driver;
import dev.zvolinskiy.cmr.entity.Passport;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;
import dev.zvolinskiy.cmr.repo.DriverRepo;
import dev.zvolinskiy.cmr.repo.PassportRepo;
import dev.zvolinskiy.cmr.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepo driverRepo;
    private final PassportRepo passportRepo;

    @Override
    public Driver save(Driver driver) {
        return driverRepo.save(driver);
    }

    @Override
    public Driver update(Driver driver) {
        Driver updatedDriver = driverRepo.findById(driver.getId()).orElse(null);
        if (updatedDriver != null) {
            updatedDriver.setLastName(driver.getLastName());
            updatedDriver.setFirstName(driver.getFirstName());
            updatedDriver.setMiddleName(driver.getMiddleName());
            updatedDriver.setPassport(driver.getPassport());
            updatedDriver.setTruck(driver.getTruck());
            updatedDriver.setTrailer(driver.getTrailer());
            return driverRepo.save(updatedDriver);
        } else {
            return driver;
        }
    }

    @Override
    public Driver findById(Integer id) throws CmrEntityNotFoundException {
        var driver = driverRepo.findById(id).orElse(null);
        if (driver != null) {
            return driver;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<Driver> findByLastName(String lastName) throws CmrEntityNotFoundException {
        List<Driver> drivers = driverRepo.findDriverByLastNameContainingIgnoreCase(lastName);
        if (!drivers.isEmpty()) {
            return drivers;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<Driver> findByTruck(String truck) throws CmrEntityNotFoundException {
        List<Driver> drivers = driverRepo.findDriverByTruckContaining(truck);
        if (!drivers.isEmpty()) {
            return drivers;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public List<Driver> findAll() {
        return (List<Driver>) driverRepo.findAll();
    }

    @Override
    public Driver findByPassport(String passportNumber) throws CmrEntityNotFoundException {
        Passport passportByNumber = passportRepo.findPassportByNumber(passportNumber.toUpperCase());
        if (passportByNumber != null) {
            return driverRepo.findDriverByPassport(passportByNumber);
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public Driver findByFullName(String fullName) throws CmrEntityNotFoundException {
        List<String> drivers = Arrays.stream(fullName.split(" ")).toList();
        var driver = driverRepo.findDriverByLastNameAndFirstNameAndMiddleName(drivers.get(0), drivers.get(1), drivers.get(2));
        if (driver != null) {
            return driver;
        } else {
            throw new CmrEntityNotFoundException();
        }
    }

    @Override
    public void delete(Driver driver) throws CmrEntityNotFoundException {
        try {
            driverRepo.delete(driver);
        } catch (IllegalArgumentException e) {
            throw new CmrEntityNotFoundException();
        }
    }
}
