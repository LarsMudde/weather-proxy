package nl.showcase.weatherproxy.application.service;

import nl.showcase.weatherproxy.application.dto.OpenWeatherMapResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class OpenWeatherMapServiceTest {

    @InjectMocks
    private OpenWeatherMapServiceImpl openWeatherMapService;

    @Mock
    RestTemplate restTemplate = new RestTemplate();

    @Mock
    ResponseEntity<OpenWeatherMapResponseDTO> mockResponseEntity;

    @Test
    void getWeatherForCityTest() {
        // Given the name of a city and an OpenWeatherMapResponseDTO
        String cityName = "Rotterdam";
        Double minimumTemperature = 21.23;
        Double maximumTemperature = 44.32;
        LocalDateTime sunrise = LocalDateTime.now().withNano(0);

        OpenWeatherMapResponseDTO weatherResponse = spy(OpenWeatherMapResponseDTO.class);
        ReflectionTestUtils.setField(weatherResponse, "minimumTemperature", minimumTemperature);
        ReflectionTestUtils.setField(weatherResponse, "maximumTemperature", maximumTemperature);
        ReflectionTestUtils.setField(weatherResponse, "sunrise", (int) sunrise.atZone(ZoneId.systemDefault()).toEpochSecond());
        ReflectionTestUtils.setField(weatherResponse, "name", cityName);

        doReturn(mockResponseEntity).when(restTemplate).exchange(contains(cityName), eq(HttpMethod.GET), any(), eq(OpenWeatherMapResponseDTO.class));
        doReturn(weatherResponse).when(mockResponseEntity).getBody();

        // When the service is called, (not sure if this can be done with var in Java 8?)
        OpenWeatherMapResponseDTO response = openWeatherMapService.getWeatherForCity(cityName);

        // Then verify the response equals the expected DTO
        assertEquals(weatherResponse, response);
    }
}
