package nl.showcase.weatherproxy.application.service;

import nl.showcase.weatherproxy.application.dto.OpenWeatherMapResponseDTO;

public interface OpenWeatherMapService {
    OpenWeatherMapResponseDTO getWeatherForCity(String city);
}
