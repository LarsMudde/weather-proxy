package nl.showcase.weatherproxy.domain.models;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "City")
@Getter
public class City implements Serializable {

    protected City() {

    }

    public City(String name, Double minimumTemperature, Double maximumTemperature, LocalDateTime sunrise) {
        this.name = name;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.sunrise = sunrise;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Double minimumTemperature;
    private Double maximumTemperature;
    private LocalDateTime sunrise;
}
