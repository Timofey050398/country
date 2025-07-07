package guru.qa.country.controller.graphql;

import guru.qa.country.domain.Country;
import guru.qa.country.domain.CountryGql;
import guru.qa.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CountryMutationController {

    private final CountryService countryService;

    @Autowired
    public CountryMutationController(CountryService countryService){
        this.countryService = countryService;
    }

    @MutationMapping
    public CountryGql addCountry(@Argument Country input){
        return countryService.addGqlCountry(input);
    }

    @MutationMapping
    public CountryGql editCountry(@Argument String countryCode, @Argument String countryName) {
        return countryService.editGqlCountryName(countryCode,countryName);
    }
}
