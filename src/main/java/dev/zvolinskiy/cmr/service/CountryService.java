package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.Country;

import java.util.List;

public interface CountryService {
    Country findCountryByName(String countryName);
    List<Country> findAll();
}
