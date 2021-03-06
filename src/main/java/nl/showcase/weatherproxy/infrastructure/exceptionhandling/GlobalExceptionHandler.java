package nl.showcase.weatherproxy.infrastructure.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public final ResponseEntity<String> handleException(HttpClientErrorException.NotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found in openweathermap api data.");
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public final ResponseEntity<String> handleException(HttpClientErrorException.Unauthorized ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<String> handleException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found in weatherproxy database, try adding it using POST: /weatherproxy/cities/{cityName}.");
    }
}
