package guru.qa.country.service;

import guru.qa.country.controller.error.ApiError;
import guru.qa.country.ex.CountryNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${api.version}")
    private String apiVersion;

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ApiError> handlePhotoNotFoundException(CountryNotFoundException ex, HttpServletRequest request) {
        LOG.error(request.getRequestURI(), ex);
        return new ResponseEntity<>(
                new ApiError(
                        apiVersion,
                        HttpStatus.NOT_FOUND.toString(),
                        "Country not found",
                        request.getRequestURI(),
                        ex.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );
    }
}
