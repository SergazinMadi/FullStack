package org.example.fullstack.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fullstack.db.dto.dto.CityDto;
import org.example.fullstack.db.dto.request.CityCreateRequest;
import org.example.fullstack.db.mapper.CityMapper;
import org.example.fullstack.db.model.City;
import org.example.fullstack.db.repository.CityRepository;
import org.example.fullstack.service.CityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    @Transactional
    public CityDto createCity(CityCreateRequest request) {
        log.info("Creating city: {}", request.name());
        
        if (cityRepository.existsByNameAndCountry(request.name(), request.country())) {
            throw new RuntimeException("City already exists: " + request.name() + ", " + request.country());
        }
        
        City city = cityMapper.cityCreateRequestToCity(request);
        City savedCity = cityRepository.save(city);
        
        log.info("City created with ID: {}", savedCity.getId());
        return cityMapper.cityToCityDto(savedCity);
    }

    @Override
    public CityDto getCity(Long cityId) {
        log.info("Getting city with ID: {}", cityId);
        
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + cityId));
        
        return cityMapper.cityToCityDto(city);
    }

    @Override
    public List<CityDto> getAllCities() {
        log.info("Getting all cities");
        
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(cityMapper::cityToCityDto)
                .toList();
    }

    @Override
    @Transactional
    public CityDto updateCity(Long cityId, CityCreateRequest request) {
        log.info("Updating city with ID: {}", cityId);
        
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + cityId));
        
        city.setName(request.name());
        city.setCountry(request.country());
        
        City updatedCity = cityRepository.save(city);
        
        log.info("City updated successfully");
        return cityMapper.cityToCityDto(updatedCity);
    }

    @Override
    @Transactional
    public void deleteCity(Long cityId) {
        log.info("Deleting city with ID: {}", cityId);
        
        if (!cityRepository.existsById(cityId)) {
            throw new RuntimeException("City not found with ID: " + cityId);
        }
        
        cityRepository.deleteById(cityId);
        log.info("City deleted successfully");
    }

    @Override
    public CityDto getCityByName(String name) {
        log.info("Getting city by name: {}", name);
        
        City city = cityRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("City not found with name: " + name));
        
        return cityMapper.cityToCityDto(city);
    }

    @Override
    public boolean existsByName(String name) {
        return cityRepository.existsByName(name);
    }
}
