package guru.qa.country.service;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.domain.Country;
import guru.qa.country.domain.CountryGql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<CountryGql> allGqlCountries(Pageable pageable) {
        return countryRepository.findAll(pageable)
                .map(country -> new CountryGql(
                        country.getId(),
                        country.getCountryName(),
                        country.getCountryCode()
                ));
    }

    @Override
    public Country addCountry(String countryName, String countryCode) {
        return addGqlCountry(
                new Country(countryName,countryCode)
        ).toJson();
    }

    @Override
    public CountryGql addGqlCountry(Country country) {
        CountryEntity ce = new CountryEntity();
        ce.setCountryCode(country.countryCode());
        ce.setCountryName(country.countryName());

        CountryEntity saved = countryRepository.save(ce);
        return new CountryGql(
                saved.getId(),
                saved.getCountryName(),
                saved.getCountryCode()
        );
    }

    @Override
    public Country editCountryName(String countryCode, String newName) {
        return editGqlCountryName(countryCode,newName).toJson();
    }

    @Override
    public CountryGql editGqlCountryName(String countryCode, String countryName) {
        CountryEntity country = countryRepository.findByCountryCode(countryCode);
        country.setCountryName(countryName);
        CountryEntity saved = countryRepository.save(country);
        return new CountryGql(
                saved.getId(),
                saved.getCountryName(),
                saved.getCountryCode()
        );
    }

}
