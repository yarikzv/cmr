package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Driver;
import dev.zvolinskiy.cmr.entity.Passport;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DriverRepo extends CrudRepository<Driver, Integer> {

    List<Driver> findDriverByLastNameContainingIgnoreCase(String lastName);

    List<Driver> findDriverByTruckContaining(String truck);

    Driver findDriverByPassport(Passport passport);

    Driver findDriverByLastNameAndFirstNameAndMiddleName(String lastName, String firstName, String middleName);
}
