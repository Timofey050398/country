package guru.qa.country.service;

import guru.qa.country.domain.Country;

import java.util.List;

public interface CountryService {

    List<Country> allCountries();

    Country addCountry(String countryName, String countryCode);

    Country editCountryName(String countryCode, String countryName);
}
