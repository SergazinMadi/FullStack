package org.example.fullstack.service;

import org.example.fullstack.db.dto.dto.CityDto;
import org.example.fullstack.db.dto.request.CityCreateRequest;

import java.util.List;

public interface CityService {
    CityDto createCity(CityCreateRequest request);
    CityDto getCity(Long cityId);
    List<CityDto> getAllCities();
    CityDto updateCity(Long cityId, CityCreateRequest request);
    void deleteCity(Long cityId);
    CityDto getCityByName(String name);
    boolean existsByName(String name);
}
