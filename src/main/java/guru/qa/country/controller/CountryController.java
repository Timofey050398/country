package guru.qa.country.controller;

import guru.qa.country.domain.Country;
import guru.qa.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/country")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService){
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public List<Country> all(){
        return countryService.allCountries();
    }

    @PostMapping("/add")
    public Country add(@RequestBody Country country){
        return countryService.addCountry(
                country.countryName(),
                country.countryCode()
        );
    }

    @PatchMapping("/{code}/name")
    public Country editByCode(@PathVariable("code") String countryCode, @RequestBody Map<String, String> country) {
        return countryService.editCountryName(countryCode,country.get("name"));
    }
}
