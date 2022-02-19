package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Driver;

import java.util.List;

public interface DriverService {

    Driver saveDriver(Driver driver);

    Driver findDriverById(Integer id);

    List<Driver> findDriverByLastName(String lastName);

    List<Driver> findDriverByTruck(String truck);

    List<Driver> findAllDrivers();

    Driver findDriverByPassport(String passportNumber);

    void deleteDriver(Driver driver);
}
