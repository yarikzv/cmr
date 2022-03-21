package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Driver;
import dev.zvolinskiy.cmr.exception.CmrEntityNotFoundException;

import java.util.List;

public interface DriverService {

    Driver save(Driver driver);

    Driver update(Driver driver);

    Driver findById(Integer id) throws CmrEntityNotFoundException;

    List<Driver> findByLastName(String lastName) throws CmrEntityNotFoundException;

    List<Driver> findByTruck(String truck) throws CmrEntityNotFoundException;

    List<Driver> findAll();

    Driver findByPassport(String passportNumber) throws CmrEntityNotFoundException;

    Driver findByFullName(String fullName) throws CmrEntityNotFoundException;

    void delete(Driver driver) throws CmrEntityNotFoundException;
}
