package nl.showcase.weatherproxy.application.service;

import nl.showcase.weatherproxy.application.dto.OpenWeatherMapResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenWeatherMapServiceImpl implements OpenWeatherMapService {

    @Value("${configurables.openweathermap.url}")
    private String openWeatherMapApiUrl;

    @Value("${configurables.openweathermap.api-key}")
    private String apiKey;

    @Value("${configurables.openweathermap.units}")
    private String units;

    private final RestTemplate restTemplate;

    public OpenWeatherMapServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public OpenWeatherMapResponseDTO getWeatherForCity(String city) {
        HttpHeaders headers = new HttpHeaders();

        return restTemplate.exchange(openWeatherMapApiUrl + "?q=" + city + "&appid=" + apiKey + "&units=" + units, HttpMethod.GET,
                    new HttpEntity<>("", headers), OpenWeatherMapResponseDTO.class).getBody();
    }
}
