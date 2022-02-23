package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.Country;
import dev.zvolinskiy.cmr.repo.CountryRepo;
import dev.zvolinskiy.cmr.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepo countryRepo;

    @Override
    public Country findCountryByName(String countryName) {
        return countryRepo.findCountryByName(countryName);
    }

    @Override
    public List<Country> findAll() {
        return (List<Country>) countryRepo.findAll();
    }
}
