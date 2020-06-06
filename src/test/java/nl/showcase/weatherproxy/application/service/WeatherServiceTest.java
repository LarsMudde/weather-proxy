package nl.showcase.weatherproxy.application.service;

import nl.showcase.weatherproxy.application.dto.OpenWeatherMapResponseDTO;
import nl.showcase.weatherproxy.domain.models.City;
import nl.showcase.weatherproxy.domain.models.Weather;
import nl.showcase.weatherproxy.infrastructure.persistence.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class WeatherServiceTest {

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private OpenWeatherMapService openWeatherMapService;

    @Autowired
    private WeatherService weatherService;

    @Test
    void addNewCityByNameTest() {
        // Given an unknown city to add and the weather there
        String cityName = "Rotterdam";
        Double minimumTemperature = 21.23;
        Double maximumTemperature = 44.32;
        LocalDateTime sunrise = LocalDateTime.now().withNano(0);

        City city = City.of(cityName);
        city.setWeather(Weather.of(minimumTemperature, maximumTemperature, sunrise));
        ReflectionTestUtils.setField(city, "id", 1L);

        OpenWeatherMapResponseDTO weatherResponse = spy(OpenWeatherMapResponseDTO.class);
        ReflectionTestUtils.setField(weatherResponse, "minimumTemperature", minimumTemperature);
        ReflectionTestUtils.setField(weatherResponse, "maximumTemperature", maximumTemperature);
        ReflectionTestUtils.setField(weatherResponse, "sunrise", (int) sunrise.atZone(ZoneId.systemDefault()).toEpochSecond());
        ReflectionTestUtils.setField(weatherResponse, "name", cityName);

        // When the weather for the city is asked from the api, no need to mock finding the city because we are testing a non existing city
        when(openWeatherMapService.getWeatherForCity(cityName)).thenReturn(weatherResponse);

        // When addNewCityByName is called
        weatherService.addCityByName(cityName);

        // Then verify it was saved
        verify(cityRepository, times(1)).findCityByNameIgnoreCase(cityName);
        verify(cityRepository, times(1)).save(city);
        verifyNoMoreInteractions(cityRepository);
    }

    @Test
    void addNewCityByNameCityExistsTest() {
        // Given an unknown city to add and the weather there
        String cityName = "Rotterdam";
        Double minimumTemperature = 21.23;
        Double maximumTemperature = 44.32;
        LocalDateTime sunrise = LocalDateTime.now().withNano(0);

        City city = City.of(cityName);
        city.setWeather(Weather.of(minimumTemperature, maximumTemperature, sunrise));
        ReflectionTestUtils.setField(city, "id", 1L);

        OpenWeatherMapResponseDTO weatherResponse = spy(OpenWeatherMapResponseDTO.class);
        ReflectionTestUtils.setField(weatherResponse, "minimumTemperature", minimumTemperature);
        ReflectionTestUtils.setField(weatherResponse, "maximumTemperature", maximumTemperature);
        ReflectionTestUtils.setField(weatherResponse, "sunrise", (int) sunrise.atZone(ZoneId.systemDefault()).toEpochSecond());
        ReflectionTestUtils.setField(weatherResponse, "name", cityName);

        // When the weather for the city is asked from the api, no need to mock finding the city because we are testing a non existing city
        when(openWeatherMapService.getWeatherForCity(cityName)).thenReturn(weatherResponse);
        when(cityRepository.findCityByNameIgnoreCase("rotterdam")).thenReturn(Optional.of(city));

        // When addNewCityByName is called
        weatherService.addCityByName(cityName);

        // Then verify it was saved
        verify(cityRepository, times(1)).findCityByNameIgnoreCase(cityName);
        verify(cityRepository, times(1)).save(city);
        verifyNoMoreInteractions(cityRepository);
    }

    @Test
    void getAllCitiesTest() {
        // Given a set two cities
        City rotterdam = City.of("Rotterdam");
        rotterdam.setWeather(Weather.of(22.15, 23.18, LocalDateTime.now().withNano(0)));
        ReflectionTestUtils.setField(rotterdam, "id", 1L);

        City amsterdam = City.of("Amsterdam");
        amsterdam.setWeather(Weather.of(11.81, 14.29, LocalDateTime.now().withNano(0).minusHours(2)));
        ReflectionTestUtils.setField(amsterdam, "id", 2L);

        HashSet<City> cities = new HashSet<>();
        cities.add(amsterdam);
        cities.add(rotterdam);

        // When cityrepository is called
        when(cityRepository.findAll()).thenReturn(cities);

        // When the service is called, (not sure if this can be done with var in Java 8?)
        HashSet<City> result = weatherService.getAllCities();

        // Then verify if the cityrepository is called and the results are returned
        verify(cityRepository, times(1)).findAll();
        verifyNoMoreInteractions(cityRepository);
        assertEquals(result, cities);
    }

    @Test
    void getAllCitiesNoCitiesTest() {
        // Given nothing

        // When the service is called, (not sure if this can be done with var in Java 8?)
        HashSet<City> result = weatherService.getAllCities();

        // Then verify if the cityrepository is called and the result is empty
        verify(cityRepository, times(1)).findAll();
        verifyNoMoreInteractions(cityRepository);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCityByNameTest() {
        // Given a city
        String cityName = "Rotterdam";
        City city = City.of(cityName);
        city.setWeather(Weather.of(22.15, 23.18, LocalDateTime.now().withNano(0)));
        ReflectionTestUtils.setField(city, "id", 1L);

        // When cityrepository is called
        when(cityRepository.findCityByNameIgnoreCase(cityName)).thenReturn(Optional.of(city));

        // When the service is called, (not sure if this can be done with var in Java 8?)
        City result = weatherService.getCityByName(cityName);

        // Then
        verify(cityRepository, times(1)).findCityByNameIgnoreCase(cityName);
        verifyNoMoreInteractions(cityRepository);
        assertEquals(city, result);
    }

    @Test
    void deleteCityByNameTest() {
        // Given a cityName
        String cityName = "Rotterdam";

        // When a city is deleted nothing happens
        weatherService.deleteCityByName(cityName);

        // Then verify the repository has been called
        verify(cityRepository, times(1)).deleteCityByNameIgnoreCase(cityName);
        verifyNoMoreInteractions(cityRepository);
    }

    @TestConfiguration
    static class WeatherServiceImplTestContextConfiguration {

        @Bean
        public WeatherService weatherService(CityRepository cityRepository, OpenWeatherMapService openWeatherMapService) {
            return new WeatherServiceImpl(cityRepository, openWeatherMapService);
        }
    }
}
