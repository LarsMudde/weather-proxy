package nl.showcase.weatherproxy.infrastructure.interfaces;

import nl.showcase.weatherproxy.application.service.WeatherService;
import nl.showcase.weatherproxy.domain.models.City;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("weatherproxy")
public class WeatherProxyController {

    private final WeatherService weatherService;

    public WeatherProxyController(WeatherService weatherService) { this.weatherService = weatherService; }

    @PostMapping("/cities/{cityName}")
    public ResponseEntity addCityByName(@PathVariable String cityName) {
        weatherService.addCityByName(cityName);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/cities")
    public ResponseEntity<HashSet<City>> getCities() {
        return ResponseEntity.ok(weatherService.getAllCities());
    }

    @GetMapping("/cities/{cityName}")
    public ResponseEntity<City> getCityByName(@PathVariable String cityName) {
        return ResponseEntity.ok(weatherService.getCityByName(cityName));
    }

    @DeleteMapping("cities/{cityName}")
    public ResponseEntity deleteCityByName(@PathVariable String cityName) {
        weatherService.deleteCityByName(cityName);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

}
