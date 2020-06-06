package nl.showcase.weatherproxy.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class OpenWeatherMapResponseDTO {

    protected OpenWeatherMapResponseDTO() {

    }

    private Double minimumTemperature;
    private Double maximumTemperature;
    private Integer sunrise;
    private String name;

    @JsonProperty("main")
    private void unpackMain(Map<String, Object> main) {
        this.minimumTemperature = Double.parseDouble(main.get("temp_min").toString());
        this.maximumTemperature = Double.parseDouble(main.get("temp_max").toString());
    }

    @JsonProperty("sys")
    private void unpackSys(Map<String, Object> sys) {
        this.sunrise = Integer.parseInt(sys.get("sunrise").toString());
    }

}
