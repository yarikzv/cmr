package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepo extends CrudRepository<Country, Integer> {
    Country findCountryByName(String countryName);
}
