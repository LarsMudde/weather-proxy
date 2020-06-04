package nl.showcase.weatherproxy.infrastructure.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public final ResponseEntity handleException(HttpClientErrorException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found in openweathermap api data.");
    }
}
