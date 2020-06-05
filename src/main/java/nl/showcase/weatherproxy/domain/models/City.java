package nl.showcase.weatherproxy.domain.models;

import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CityWeather")
@Getter
public class City implements Serializable {

    protected City() {

    }

    public static City of(String name) {
        Assert.hasText(name, "name is obligated!");

        City city = new City();
        city.name = name;
        return city;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String name;

    @Embedded
    private Weather weather;
}
