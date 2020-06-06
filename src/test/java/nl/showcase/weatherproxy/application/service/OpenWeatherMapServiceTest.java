package nl.showcase.weatherproxy.application.service;

import nl.showcase.weatherproxy.application.dto.OpenWeatherMapResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import org.springframework.beans.factory.annotation.Value;
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

    private String openWeatherMapApiUrl;

    private String apiKey;

    private String units;

    @BeforeEach
    void testSetup() {
        openWeatherMapApiUrl = "https://api.openweathermap.org/data/2.5/weather";
        ReflectionTestUtils.setField(openWeatherMapService, "openWeatherMapApiUrl", openWeatherMapApiUrl);

        apiKey = "abc123def";
        ReflectionTestUtils.setField(openWeatherMapService, "apiKey", apiKey);

        units = "metric";
        ReflectionTestUtils.setField(openWeatherMapService, "units", units);
    }


    @Test
    void getWeatherForCityTest() {
        // Given the name of a city and an OpenWeatherMapResponseDTO
        String cityName = "Rotterdam";
        Double minimumTemperature = 21.23;
        Double maximumTemperature = 44.32;
        LocalDateTime sunrise = LocalDateTime.now().withNano(0);

        OpenWeatherMapResponseDTO expectedResponse = spy(OpenWeatherMapResponseDTO.class);
        ReflectionTestUtils.setField(expectedResponse, "minimumTemperature", minimumTemperature);
        ReflectionTestUtils.setField(expectedResponse, "maximumTemperature", maximumTemperature);
        ReflectionTestUtils.setField(expectedResponse, "sunrise", (int) sunrise.atZone(ZoneId.systemDefault()).toEpochSecond());
        ReflectionTestUtils.setField(expectedResponse, "name", cityName);

        String expectedUrl = openWeatherMapApiUrl + "?q=" + cityName + "&appid=" + apiKey + "&units=" + units;

        doReturn(mockResponseEntity).when(restTemplate).exchange(eq(expectedUrl), eq(HttpMethod.GET), any(), eq(OpenWeatherMapResponseDTO.class));
        doReturn(expectedResponse).when(mockResponseEntity).getBody();

        // When the service is called
        OpenWeatherMapResponseDTO response = openWeatherMapService.getWeatherForCity(cityName);

        // Then verify the response equals the expected DTO
        assertEquals(expectedResponse, response);
    }
}
