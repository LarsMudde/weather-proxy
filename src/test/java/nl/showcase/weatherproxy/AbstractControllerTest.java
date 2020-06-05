package nl.showcase.weatherproxy;

import nl.showcase.weatherproxy.application.service.WeatherService;
import nl.showcase.weatherproxy.infrastructure.interfaces.WeatherProxyController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = {WeatherProxyController.class})
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected WeatherService weatherService;
}
