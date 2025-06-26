package guru.qa.country.service;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbCountryService implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public DbCountryService(CountryRepository countryRepository){
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> allCountries() {
        return countryRepository.findAll()
                .stream()
                .map(CountryEntity::toJson)
                .toList();
    }

    @Override
    public Country addCountry(String countryName, String countryCode) {
        return countryRepository.save(
                new CountryEntity(
                        null,
                        countryName,
                        countryCode
                )
        ).toJson();
    }

    @Override
    public Country editCountryName(String countryCode, String newName) {
        CountryEntity country = countryRepository.findByCountryCode(countryCode);
        country.setCountryName(newName);
        return countryRepository.save(country).toJson();
    }

}
