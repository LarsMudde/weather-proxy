package nl.showcase.weatherproxy.application.service;

import nl.showcase.weatherproxy.domain.models.City;

import java.util.HashSet;

public interface WeatherService {

    void addCityByName(String cityName);
    HashSet<City> getAllCities();
    City getCityByName(String cityName);
    void deleteCityByName(String cityName);
}
