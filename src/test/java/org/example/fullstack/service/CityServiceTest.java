package org.example.fullstack.service;

import org.example.fullstack.db.dto.dto.CityDto;
import org.example.fullstack.db.dto.request.CityCreateRequest;
import org.example.fullstack.db.mapper.CityMapper;
import org.example.fullstack.db.model.City;
import org.example.fullstack.db.repository.CityRepository;
import org.example.fullstack.service.impl.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityServiceImpl cityService;

    private City testCity;
    private CityCreateRequest cityCreateRequest;
    private CityDto cityDto;

    @BeforeEach
    void setUp() {
        testCity = new City();
        testCity.setId(1L);
        testCity.setName("Almaty");
        testCity.setCountry("Kazakhstan");

        cityCreateRequest = new CityCreateRequest(
                "Almaty",
                "Kazakhstan"
        );

        cityDto = new CityDto(
                1L,
                "Almaty",
                "Kazakhstan"
        );
    }

    @Test
    void createCity_ShouldReturnCityDto_WhenCityDoesNotExist() {
        // Given
        when(cityRepository.existsByNameAndCountry(anyString(), anyString())).thenReturn(false);
        when(cityMapper.cityCreateRequestToCity(any(CityCreateRequest.class))).thenReturn(testCity);
        when(cityRepository.save(any(City.class))).thenReturn(testCity);
        when(cityMapper.cityToCityDto(any(City.class))).thenReturn(cityDto);

        // When
        CityDto result = cityService.createCity(cityCreateRequest);

        // Then
        assertNotNull(result);
        assertEquals(cityDto, result);
        verify(cityRepository).existsByNameAndCountry("Almaty", "Kazakhstan");
        verify(cityMapper).cityCreateRequestToCity(cityCreateRequest);
        verify(cityRepository).save(testCity);
        verify(cityMapper).cityToCityDto(testCity);
    }

    @Test
    void createCity_ShouldThrowException_WhenCityAlreadyExists() {
        // Given
        when(cityRepository.existsByNameAndCountry(anyString(), anyString())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            cityService.createCity(cityCreateRequest);
        });

        verify(cityRepository).existsByNameAndCountry("Almaty", "Kazakhstan");
        verify(cityMapper, never()).cityCreateRequestToCity(any());
        verify(cityRepository, never()).save(any());
    }

    @Test
    void getCity_ShouldReturnCityDto_WhenCityExists() {
        // Given
        when(cityRepository.findById(1L)).thenReturn(Optional.of(testCity));
        when(cityMapper.cityToCityDto(any(City.class))).thenReturn(cityDto);

        // When
        CityDto result = cityService.getCity(1L);

        // Then
        assertNotNull(result);
        assertEquals(cityDto, result);
        verify(cityRepository).findById(1L);
        verify(cityMapper).cityToCityDto(testCity);
    }

    @Test
    void getCity_ShouldThrowException_WhenCityNotFound() {
        // Given
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            cityService.getCity(1L);
        });

        verify(cityRepository).findById(1L);
        verify(cityMapper, never()).cityToCityDto(any());
    }

    @Test
    void getAllCities_ShouldReturnListOfCityDtos() {
        // Given
        List<City> cities = List.of(testCity);
        when(cityRepository.findAll()).thenReturn(cities);
        when(cityMapper.cityToCityDto(any(City.class))).thenReturn(cityDto);

        // When
        List<CityDto> result = cityService.getAllCities();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cityDto, result.get(0));
        verify(cityRepository).findAll();
        verify(cityMapper).cityToCityDto(testCity);
    }

    @Test
    void updateCity_ShouldReturnUpdatedCityDto_WhenCityExists() {
        // Given
        CityCreateRequest updateRequest = new CityCreateRequest("Updated City", "Updated Country");
        when(cityRepository.findById(1L)).thenReturn(Optional.of(testCity));
        when(cityRepository.save(any(City.class))).thenReturn(testCity);
        when(cityMapper.cityToCityDto(any(City.class))).thenReturn(cityDto);

        // When
        CityDto result = cityService.updateCity(1L, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals(cityDto, result);
        verify(cityRepository).findById(1L);
        verify(cityRepository).save(testCity);
        verify(cityMapper).cityToCityDto(testCity);
    }

    @Test
    void updateCity_ShouldThrowException_WhenCityNotFound() {
        // Given
        CityCreateRequest updateRequest = new CityCreateRequest("Updated City", "Updated Country");
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            cityService.updateCity(1L, updateRequest);
        });

        verify(cityRepository).findById(1L);
        verify(cityRepository, never()).save(any());
    }

    @Test
    void deleteCity_ShouldDeleteCity_WhenCityExists() {
        // Given
        when(cityRepository.existsById(1L)).thenReturn(true);

        // When
        cityService.deleteCity(1L);

        // Then
        verify(cityRepository).existsById(1L);
        verify(cityRepository).deleteById(1L);
    }

    @Test
    void deleteCity_ShouldThrowException_WhenCityNotFound() {
        // Given
        when(cityRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            cityService.deleteCity(1L);
        });

        verify(cityRepository).existsById(1L);
        verify(cityRepository, never()).deleteById(anyLong());
    }

    @Test
    void getCityByName_ShouldReturnCityDto_WhenCityExists() {
        // Given
        when(cityRepository.findByName("Almaty")).thenReturn(Optional.of(testCity));
        when(cityMapper.cityToCityDto(any(City.class))).thenReturn(cityDto);

        // When
        CityDto result = cityService.getCityByName("Almaty");

        // Then
        assertNotNull(result);
        assertEquals(cityDto, result);
        verify(cityRepository).findByName("Almaty");
        verify(cityMapper).cityToCityDto(testCity);
    }

    @Test
    void getCityByName_ShouldThrowException_WhenCityNotFound() {
        // Given
        when(cityRepository.findByName("Almaty")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            cityService.getCityByName("Almaty");
        });

        verify(cityRepository).findByName("Almaty");
        verify(cityMapper, never()).cityToCityDto(any());
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenCityExists() {
        // Given
        when(cityRepository.existsByName("Almaty")).thenReturn(true);

        // When
        boolean result = cityService.existsByName("Almaty");

        // Then
        assertTrue(result);
        verify(cityRepository).existsByName("Almaty");
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenCityDoesNotExist() {
        // Given
        when(cityRepository.existsByName("Almaty")).thenReturn(false);

        // When
        boolean result = cityService.existsByName("Almaty");

        // Then
        assertFalse(result);
        verify(cityRepository).existsByName("Almaty");
    }
}
