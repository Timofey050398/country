package guru.qa.country.config;

import guru.qa.country.service.CountryErrorAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${api.version}")
    private String apiVersion;

    @Bean
    public ErrorAttributes errorAttributes() {
        return new CountryErrorAttributes(apiVersion);
    }
}