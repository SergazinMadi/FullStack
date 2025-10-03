package org.example.fullstack.db.repository;

import org.example.fullstack.db.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
