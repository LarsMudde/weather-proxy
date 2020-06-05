package nl.showcase.weatherproxy.application.service;

import nl.showcase.weatherproxy.application.dto.OpenWeatherMapResponseDTO;
import nl.showcase.weatherproxy.domain.models.City;
import nl.showcase.weatherproxy.domain.models.Weather;
import nl.showcase.weatherproxy.infrastructure.persistence.repository.CityRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.TimeZone;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService{

    private final CityRepository cityRepository;
    private final OpenWeatherMapService openWeatherMapService;

    public WeatherServiceImpl(CityRepository cityRepository, OpenWeatherMapService openWeatherMapService) {
        this.cityRepository = cityRepository;
        this.openWeatherMapService = openWeatherMapService;
    }

    @Override
    public void addCityByName(String cityName) {
       OpenWeatherMapResponseDTO openWeatherMapResponse = openWeatherMapService.getWeatherForCity(cityName);
       City city = cityRepository.findCityByNameIgnoreCase(openWeatherMapResponse.getName()).orElse(City.of(openWeatherMapResponse.getName()));
       city.setWeather(Weather.of(openWeatherMapResponse.getMinimumTemperature(), openWeatherMapResponse.getMaximumTemperature(), LocalDateTime.ofInstant(Instant.ofEpochSecond(openWeatherMapResponse.getSunrise()), TimeZone.getDefault().toZoneId())));
       cityRepository.save(city);
    }

    @Override
    public HashSet<City> getAllCities() {
        HashSet<City> cities = new HashSet<>();
        cityRepository.findAll().forEach(cities::add);
        return cities;
    }

    @Override
    public City getCityByName(String cityName) {
        return cityRepository.findCityByNameIgnoreCase(cityName).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void deleteCityByName(String cityName) {
        cityRepository.deleteCityByNameIgnoreCase(cityName);
    }

}
