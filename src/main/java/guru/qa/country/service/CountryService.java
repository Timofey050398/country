package guru.qa.country.service;

import guru.qa.country.domain.Country;
import guru.qa.country.domain.CountryGql;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CountryService {

    List<Country> allCountries();

    Slice<CountryGql> allGqlCountries(Pageable pageable);

    Country addCountry(String countryName, String countryCode);

    CountryGql addGqlCountry(Country country);

    Country editCountryName(String countryCode, String countryName);

    CountryGql editGqlCountryName(String countryCode, String countryName);
}
