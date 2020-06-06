package nl.showcase.weatherproxy.domain.models;

import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;


@Embeddable
@Getter
public class Weather implements Serializable {

    protected Weather() {

    }

    public static Weather of(Double minimumTemperature, Double maximumTemperature, LocalDateTime sunrise) {
        Assert.notNull(minimumTemperature, "No minimum temperature found");
        Assert.notNull(maximumTemperature, "No maximum temperature found");
        Assert.notNull(sunrise, "No sunrise found");

        Weather weather = new Weather();
        weather.minimumTemperature = minimumTemperature;
        weather.maximumTemperature = maximumTemperature;
        weather.sunrise = sunrise;

        return weather;
    }

    private Double minimumTemperature;
    private Double maximumTemperature;
    private LocalDateTime sunrise;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weather)) return false;
        Weather weather = (Weather) o;
        return getMinimumTemperature().equals(weather.getMinimumTemperature()) &&
                getMaximumTemperature().equals(weather.getMaximumTemperature()) &&
                getSunrise().equals(weather.getSunrise());
    }
}
