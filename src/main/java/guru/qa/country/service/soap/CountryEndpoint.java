package guru.qa.country.service.soap;


import guru.qa.country.config.AppConfig;
import guru.qa.country.service.CountryService;
import guru.qa.xml.country.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CountryEndpoint {

    private final CountryService countryService;

    public CountryEndpoint (CountryService countryService) {
        this.countryService = countryService;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "allRequest")
    @ResponsePayload
    public AllResponse all() {
        AllResponse response = new AllResponse();
        response.getCountries()
                .addAll(
                        countryService.allCountries()
                                .stream()
                                .map(country -> {
                                    Country countryXml = new Country();
                                countryXml.setCountryName(country.countryName());
                                countryXml.setCountryCode(country.countryCode());
                                return countryXml;}
                                )
                                .toList()
                );
        return response;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "addRequest")
    @ResponsePayload
    public AddResponse add(@RequestPayload AddRequest request) {
        guru.qa.country.domain.Country country = countryService.addCountry(
                request.getCountry().getCountryName(),
                request.getCountry().getCountryCode()
        );
        AddResponse response = new AddResponse();
        Country countryXml = new Country();
        countryXml.setCountryName(country.countryName());
        countryXml.setCountryCode(country.countryCode());
        response.setCountry(countryXml);
        return response;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "editByCodeRequest")
    @ResponsePayload
    public EditByCodeResponse editByCode(@RequestPayload EditByCodeRequest request) {
        guru.qa.country.domain.Country country = countryService.editCountryName(
                request.getCode(),
                request.getName()
        );
        EditByCodeResponse response = new EditByCodeResponse();
        Country countryXml = new Country();
        countryXml.setCountryName(country.countryName());
        countryXml.setCountryCode(country.countryCode());
        response.setCountry(countryXml);
        return response;
    }
}
