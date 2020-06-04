package nl.showcase.weatherproxy.infrastructure.persistence.repository;

import nl.showcase.weatherproxy.domain.models.City;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CityRepository extends CrudRepository<City, Long> {
    Optional<City> findCityByNameIgnoreCase(String name);
    void deleteCityByNameIgnoreCase(String name);
}
