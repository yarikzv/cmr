package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Driver;
import dev.zvolinskiy.cmr.entity.Passport;
import dev.zvolinskiy.cmr.repo.DriverRepo;
import dev.zvolinskiy.cmr.repo.PassportRepo;
import dev.zvolinskiy.cmr.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    public Driver saveDriver(Driver driver) {
        return driverRepo.save(driver);
    }

    @Override
    public Driver findDriverById(Integer id) {
        return driverRepo.findById(id).orElse(null);
    }

    @Override
    public List<Driver> findDriverByLastName(String lastName) {
        return driverRepo.findDriverByLastNameContaining(lastName);
    }

    @Override
    public List<Driver> findDriverByTruck(String truck) {
        return driverRepo.findDriverByTruckContaining(truck);
    }

    @Override
    public List<Driver> findAllDrivers() {
        return (List<Driver>) driverRepo.findAll();
    }

    @Override
    public Driver findDriverByPassport(String passportNumber) {
        Passport passportByNumber = passportRepo.findPassportByNumber(passportNumber.toUpperCase());
        if (passportByNumber != null) {
            return driverRepo.findDriverByPassport(passportByNumber);
        } else {
            throw new EntityNotFoundException("Driver not found");
        }
    }

    @Override
    public Driver findDriverByFullName(String fullName) {
        List<String> driver = Arrays.stream(fullName.split(" ")).toList();
        return driverRepo.findDriverByLastNameAndFirstNameAndMiddleName(driver.get(0), driver.get(1), driver.get(2));
    }

    @Override
    public void deleteDriver(Driver driver) {
        driverRepo.delete(driver);
    }
}
