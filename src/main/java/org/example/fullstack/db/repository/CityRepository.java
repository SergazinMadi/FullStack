package org.example.fullstack.db.repository;

import org.example.fullstack.db.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);
    
    Optional<City> findByNameAndCountry(String name, String country);
    
    boolean existsByName(String name);
    
    boolean existsByNameAndCountry(String name, String country);
}