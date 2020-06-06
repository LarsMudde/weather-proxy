package nl.showcase.weatherproxy.infrastructure.interfaces;

import nl.showcase.weatherproxy.application.service.WeatherService;
import nl.showcase.weatherproxy.domain.models.City;
import nl.showcase.weatherproxy.domain.models.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = {WeatherProxyController.class})
public class WeatherProxyControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected WeatherService weatherService;

    @Test
    void addCityByNameTest() throws Exception {
        // Given the name of a city to add
        String cityName = "Rotterdam";

        // When addCityByName is called Then reply with NO_CONTENT status ok
        mockMvc.perform(post("/weatherproxy/cities/" + cityName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("NO_CONTENT")));
    }

    @Test
    void addCityByNameNoAPIKeyTest() throws Exception {
        // Given the name of a city to add
        String cityName = "Rotterdam";

        // When addCityByName is called
        willThrow(HttpClientErrorException.Unauthorized.class).given(weatherService).addCityByName(cityName);

        // When addCityByName is called then reply with 404 not found
        mockMvc.perform(post("/weatherproxy/cities/" + cityName))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addCityByNameCityNotFoundInOpenWeatherMapTest() throws Exception {
        // Given the name of a city to add
        String cityName = "Rotterdam";

        // When addCityByName is called
        willThrow(HttpClientErrorException.NotFound.class).given(weatherService).addCityByName(cityName);

        // When addCityByName is called then reply with 404 not found
        mockMvc.perform(post("/weatherproxy/cities/" + cityName))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCitiesTest() throws Exception {
        // Given a set with 2 Cities
        City amsterdam = City.of("Amsterdam");
        amsterdam.setWeather(Weather.of(11.2, 13.58, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));

        City rotterdam = City.of("Rotterdam");
        rotterdam.setWeather(Weather.of(10.51, 13.99, LocalDateTime.now().minusHours(10L).truncatedTo(ChronoUnit.SECONDS)));

        HashSet<City> cities = new HashSet<>(Arrays.asList(amsterdam, rotterdam));

        // When WeatherService.GetAllCities is called return the list of cities
        when(weatherService.getAllCities()).thenReturn(cities);

        // When GET: /weatherproxy/cities is called Then expect a list of the cities and status 200
        mockMvc.perform(get("/weatherproxy/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(rotterdam.getName(), amsterdam.getName())))
                .andExpect(jsonPath("$[*].weather.minimumTemperature", containsInAnyOrder(rotterdam.getWeather().getMinimumTemperature(), amsterdam.getWeather().getMinimumTemperature())))
                .andExpect(jsonPath("$[*].weather.maximumTemperature", containsInAnyOrder(rotterdam.getWeather().getMaximumTemperature(), amsterdam.getWeather().getMaximumTemperature())))
                .andExpect(jsonPath("$[*].weather.sunrise", containsInAnyOrder(rotterdam.getWeather().getSunrise().toString(), amsterdam.getWeather().getSunrise().toString())));
    }

    @Test
    void getCitiesNoCitiesTest() throws Exception {
        // Given an empty set
        HashSet<City> cities = new HashSet<>();

        // When WeatherService.GetAllCities is called return the list of cities
        when(weatherService.getAllCities()).thenReturn(cities);

        // When GET: /weatherproxy/cities is called Then expect a list of the cities and status 200
        mockMvc.perform(get("/weatherproxy/cities"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void getCityByNameTest() throws Exception {
        // Given a city
        City rotterdam = City.of("Rotterdam");
        rotterdam.setWeather(Weather.of(10.51, 13.99, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));

        // When WeatherService.GetAllCities is called return the list of cities
        when(weatherService.getCityByName(rotterdam.getName())).thenReturn(rotterdam);

        // When GET: /weatherproxy/cities/{city} is called Then expect a list of the cities and status 200
        mockMvc.perform(get("/weatherproxy/cities/Rotterdam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(rotterdam.getName())))
                .andExpect(jsonPath("$.weather.minimumTemperature", is(rotterdam.getWeather().getMinimumTemperature())))
                .andExpect(jsonPath("$.weather.maximumTemperature", is(rotterdam.getWeather().getMaximumTemperature())))
                .andExpect(jsonPath("$.weather.sunrise", is(rotterdam.getWeather().getSunrise().toString())));
    }

    @Test
    void getCityByNameCityNotFoundTest() throws Exception {
        // Given the name of a city to find
        String cityName = "Rotterdam";

        // When requested a city, throw an exception
        when(weatherService.getCityByName(anyString())).thenThrow(NoSuchElementException.class);

        // When GET: /weatherproxy/cities is called Then expect a list of the cities and status 200
        mockMvc.perform(get("/weatherproxy/cities/" + cityName))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCityByNameTest() throws Exception {
        // Given the name of a city to add
        String cityName = "Rotterdam";

        // When addCityByName is called Then reply with NO_CONTENT status ok
        mockMvc.perform(delete("/weatherproxy/cities/" + cityName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("NO_CONTENT")));
    }

}
