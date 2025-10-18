package org.example.fullstack.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstack.db.dto.dto.CityDto;
import org.example.fullstack.db.dto.request.CityCreateRequest;
import org.example.fullstack.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @PostMapping
    public ResponseEntity<CityDto> createCity(@RequestBody CityCreateRequest request) {
        CityDto city = cityService.createCity(request);
        return ResponseEntity.ok(city);
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<CityDto> getCity(@PathVariable Long cityId) {
        CityDto city = cityService.getCity(cityId);
        return ResponseEntity.ok(city);
    }

    @GetMapping
    public ResponseEntity<List<CityDto>> getAllCities() {
        List<CityDto> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CityDto> getCityByName(@PathVariable String name) {
        CityDto city = cityService.getCityByName(name);
        return ResponseEntity.ok(city);
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<CityDto> updateCity(@PathVariable Long cityId, 
                                             @RequestBody CityCreateRequest request) {
        CityDto city = cityService.updateCity(cityId, request);
        return ResponseEntity.ok(city);
    }

    @DeleteMapping("/{cityId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCity(@PathVariable Long cityId) {
        cityService.deleteCity(cityId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> checkCityExists(@PathVariable String name) {
        boolean exists = cityService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
}
