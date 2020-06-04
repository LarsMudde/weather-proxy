package nl.showcase.weatherproxy.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
public class OpenWeatherMapResponseDTO {

    private Double minimumTemperature;
    private Double maximumTemperature;
    private Integer sunrise;
    private String name;

    @JsonProperty("main")
    private void unpackMain(Map<String, Object> main) {
        this.minimumTemperature = (double) main.get("temp_min");
        this.maximumTemperature = (double) main.get("temp_max");
    }

    @JsonProperty("sys")
    private void unpackSys(Map<String, Object> sys) {
        this.sunrise = (int) sys.get("sunrise");
    }

}
