package guru.qa.country.domain;

import java.util.UUID;

public record CountryGql(UUID id,
                         String countryName,
                         String countryCode) {

    public Country toJson(){
        return new Country(
                countryName,
                countryCode
        );
    }
}
