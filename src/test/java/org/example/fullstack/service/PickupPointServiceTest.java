package org.example.fullstack.service;

import org.example.fullstack.db.dto.dto.PickupPointDto;
import org.example.fullstack.db.dto.request.PickupPointCreateRequest;
import org.example.fullstack.db.dto.request.PickupPointUpdateRequest;
import org.example.fullstack.db.enums.PickupPointType;
import org.example.fullstack.db.mapper.PickupPointMapper;
import org.example.fullstack.db.model.City;
import org.example.fullstack.db.model.PickupPoint;
import org.example.fullstack.db.repository.CityRepository;
import org.example.fullstack.db.repository.PickupPointRepository;
import org.example.fullstack.service.impl.PickupPointServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PickupPointServiceTest {

    @Mock
    private PickupPointRepository pickupPointRepository;

    @Mock
    private PickupPointMapper pickupPointMapper;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private PickupPointServiceImpl pickupPointService;

    private City testCity;
    private PickupPoint testPickupPoint;
    private PickupPointCreateRequest pickupPointCreateRequest;
    private PickupPointUpdateRequest pickupPointUpdateRequest;
    private PickupPointDto pickupPointDto;

    @BeforeEach
    void setUp() {
        testCity = new City();
        testCity.setId(1L);
        testCity.setName("Almaty");
        testCity.setCountry("Kazakhstan");

        testPickupPoint = new PickupPoint();
        testPickupPoint.setId(1L);
        testPickupPoint.setName("Test Pickup Point");
        testPickupPoint.setAddress("Test Address");
        testPickupPoint.setCity(testCity);
        testPickupPoint.setPhone("+7 777 123 4567");
        testPickupPoint.setWorkingHours("9:00-18:00");
        testPickupPoint.setLatitude(new BigDecimal("43.2220"));
        testPickupPoint.setLongitude(new BigDecimal("76.8512"));
        testPickupPoint.setMaxCapacity(100);
        testPickupPoint.setCurrentLoad(0);
        testPickupPoint.setMaxPackageWeight(50.0);
        testPickupPoint.setHasColdStorage(true);
        testPickupPoint.setIsActive(true);
        testPickupPoint.setType(PickupPointType.WAREHOUSE);

        pickupPointCreateRequest = new PickupPointCreateRequest(
                "Test Pickup Point",
                "Test Address",
                1L, // cityId
                "+7 777 123 4567",
                "9:00-18:00",
                new BigDecimal("43.2220"),
                new BigDecimal("76.8512"),
                100, // maxCapacity
                50.0, // maxPackageWeight
                true, // hasColdStorage
                PickupPointType.WAREHOUSE,
                1L // managerId
        );

        pickupPointUpdateRequest = new PickupPointUpdateRequest(
                "Updated Pickup Point",
                "Updated Address",
                1L, // cityId
                "+7 777 123 4567",
                "9:00-18:00",
                new BigDecimal("43.2220"),
                new BigDecimal("76.8512"),
                100, // maxCapacity
                50.0, // maxPackageWeight
                true, // hasColdStorage
                true, // isActive
                PickupPointType.WAREHOUSE,
                1L // managerId
        );

        pickupPointDto = new PickupPointDto(
                1L,
                "Test Pickup Point",
                "Test Address",
                1L, // cityId
                "+7 777 123 4567",
                "9:00-18:00",
                new BigDecimal("43.2220"),
                new BigDecimal("76.8512"),
                100, // maxCapacity
                0, // currentLoad
                50.0, // maxPackageWeight
                true, // hasColdStorage
                true, // isActive
                PickupPointType.WAREHOUSE,
                1L, // managerId
                null, // createdAt
                null // updatedAt
        );
    }

    @Test
    void createPickupPoint_ShouldReturnPickupPointDto_WhenValidRequest() {
        // Given
        when(cityRepository.findById(1L)).thenReturn(Optional.of(testCity));
        when(pickupPointMapper.pickupPointCreateRequestToPickupPoint(any(PickupPointCreateRequest.class)))
                .thenReturn(testPickupPoint);
        when(pickupPointRepository.save(any(PickupPoint.class))).thenReturn(testPickupPoint);
        when(pickupPointMapper.pickupPointToPickupPointDto(any(PickupPoint.class))).thenReturn(pickupPointDto);

        // When
        PickupPointDto result = pickupPointService.createPickupPoint(pickupPointCreateRequest);

        // Then
        assertNotNull(result);
        assertEquals(pickupPointDto, result);
        verify(cityRepository).findById(1L);
        verify(pickupPointMapper).pickupPointCreateRequestToPickupPoint(pickupPointCreateRequest);
        verify(pickupPointRepository).save(testPickupPoint);
        verify(pickupPointMapper).pickupPointToPickupPointDto(testPickupPoint);
    }

    @Test
    void createPickupPoint_ShouldThrowException_WhenCityNotFound() {
        // Given
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            pickupPointService.createPickupPoint(pickupPointCreateRequest);
        });

        verify(cityRepository).findById(1L);
        verify(pickupPointMapper, never()).pickupPointCreateRequestToPickupPoint(any());
        verify(pickupPointRepository, never()).save(any());
    }

    @Test
    void updatePickupPoint_ShouldReturnUpdatedPickupPointDto_WhenPickupPointExists() {
        // Given
        when(pickupPointRepository.findById(1L)).thenReturn(Optional.of(testPickupPoint));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(testCity));
        when(pickupPointRepository.save(any(PickupPoint.class))).thenReturn(testPickupPoint);
        when(pickupPointMapper.pickupPointToPickupPointDto(any(PickupPoint.class))).thenReturn(pickupPointDto);

        // When
        PickupPointDto result = pickupPointService.updatePickupPoint(1L, pickupPointUpdateRequest);

        // Then
        assertNotNull(result);
        assertEquals(pickupPointDto, result);
        verify(pickupPointRepository).findById(1L);
        verify(cityRepository).findById(1L);
        verify(pickupPointMapper).updatePickupPointFromRequest(pickupPointUpdateRequest, testPickupPoint);
        verify(pickupPointRepository).save(testPickupPoint);
        verify(pickupPointMapper).pickupPointToPickupPointDto(testPickupPoint);
    }

    @Test
    void updatePickupPoint_ShouldThrowException_WhenPickupPointNotFound() {
        // Given
        when(pickupPointRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            pickupPointService.updatePickupPoint(1L, pickupPointUpdateRequest);
        });

        verify(pickupPointRepository).findById(1L);
        verify(cityRepository, never()).findById(anyLong());
        verify(pickupPointMapper, never()).updatePickupPointFromRequest(any(), any());
        verify(pickupPointRepository, never()).save(any());
    }

    @Test
    void getPickupPoint_ShouldReturnPickupPointDto_WhenPickupPointExists() {
        // Given
        when(pickupPointRepository.findById(1L)).thenReturn(Optional.of(testPickupPoint));
        when(pickupPointMapper.pickupPointToPickupPointDto(any(PickupPoint.class))).thenReturn(pickupPointDto);

        // When
        PickupPointDto result = pickupPointService.getPickupPoint(1L);

        // Then
        assertNotNull(result);
        assertEquals(pickupPointDto, result);
        verify(pickupPointRepository).findById(1L);
        verify(pickupPointMapper).pickupPointToPickupPointDto(testPickupPoint);
    }

    @Test
    void getPickupPoint_ShouldThrowException_WhenPickupPointNotFound() {
        // Given
        when(pickupPointRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            pickupPointService.getPickupPoint(1L);
        });

        verify(pickupPointRepository).findById(1L);
        verify(pickupPointMapper, never()).pickupPointToPickupPointDto(any());
    }

    @Test
    void getAllPickupPoints_ShouldReturnListOfPickupPointDtos() {
        // Given
        List<PickupPoint> pickupPoints = List.of(testPickupPoint);
        when(pickupPointRepository.findAll()).thenReturn(pickupPoints);
        when(pickupPointMapper.pickupPointToPickupPointDto(any(PickupPoint.class))).thenReturn(pickupPointDto);

        // When
        List<PickupPointDto> result = pickupPointService.getAllPickupPoints();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pickupPointDto, result.get(0));
        verify(pickupPointRepository).findAll();
        verify(pickupPointMapper).pickupPointToPickupPointDto(testPickupPoint);
    }

    @Test
    void getPickupPointsByCity_ShouldReturnListOfPickupPointDtos_WhenCityExists() {
        // Given
        List<PickupPoint> pickupPoints = List.of(testPickupPoint);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(testCity));
        when(pickupPointRepository.findByCityAndIsActive(testCity, true)).thenReturn(pickupPoints);
        when(pickupPointMapper.pickupPointToPickupPointDto(any(PickupPoint.class))).thenReturn(pickupPointDto);

        // When
        List<PickupPointDto> result = pickupPointService.getPickupPointsByCity(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pickupPointDto, result.get(0));
        verify(cityRepository).findById(1L);
        verify(pickupPointRepository).findByCityAndIsActive(testCity, true);
        verify(pickupPointMapper).pickupPointToPickupPointDto(testPickupPoint);
    }

    @Test
    void getPickupPointsByCity_ShouldThrowException_WhenCityNotFound() {
        // Given
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            pickupPointService.getPickupPointsByCity(1L);
        });

        verify(cityRepository).findById(1L);
        verify(pickupPointRepository, never()).findByCityAndIsActive(any(), any());
    }

    @Test
    void getPickupPointsByType_ShouldReturnListOfPickupPointDtos() {
        // Given
        List<PickupPoint> pickupPoints = List.of(testPickupPoint);
        when(pickupPointRepository.findByType(PickupPointType.WAREHOUSE)).thenReturn(pickupPoints);
        when(pickupPointMapper.pickupPointToPickupPointDto(any(PickupPoint.class))).thenReturn(pickupPointDto);

        // When
        List<PickupPointDto> result = pickupPointService.getPickupPointsByType(PickupPointType.WAREHOUSE);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pickupPointDto, result.get(0));
        verify(pickupPointRepository).findByType(PickupPointType.WAREHOUSE);
        verify(pickupPointMapper).pickupPointToPickupPointDto(testPickupPoint);
    }

    @Test
    void getAvailablePickupPoints_ShouldReturnListOfAvailablePickupPointDtos_WhenCityExists() {
        // Given
        List<PickupPoint> pickupPoints = List.of(testPickupPoint);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(testCity));
        when(pickupPointRepository.findAvailablePickupPointsInCity(testCity)).thenReturn(pickupPoints);
        when(pickupPointMapper.pickupPointToPickupPointDto(any(PickupPoint.class))).thenReturn(pickupPointDto);

        // When
        List<PickupPointDto> result = pickupPointService.getAvailablePickupPoints(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pickupPointDto, result.get(0));
        verify(cityRepository).findById(1L);
        verify(pickupPointRepository).findAvailablePickupPointsInCity(testCity);
        verify(pickupPointMapper).pickupPointToPickupPointDto(testPickupPoint);
    }

    @Test
    void getAvailableColdStoragePickupPoints_ShouldReturnListOfColdStoragePickupPointDtos_WhenCityExists() {
        // Given
        List<PickupPoint> pickupPoints = List.of(testPickupPoint);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(testCity));
        when(pickupPointRepository.findAvailableColdStoragePickupPointsInCity(testCity)).thenReturn(pickupPoints);
        when(pickupPointMapper.pickupPointToPickupPointDto(any(PickupPoint.class))).thenReturn(pickupPointDto);

        // When
        List<PickupPointDto> result = pickupPointService.getAvailableColdStoragePickupPoints(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pickupPointDto, result.get(0));
        verify(cityRepository).findById(1L);
        verify(pickupPointRepository).findAvailableColdStoragePickupPointsInCity(testCity);
        verify(pickupPointMapper).pickupPointToPickupPointDto(testPickupPoint);
    }

    @Test
    void deletePickupPoint_ShouldDeletePickupPoint_WhenPickupPointExists() {
        // Given
        when(pickupPointRepository.existsById(1L)).thenReturn(true);

        // When
        pickupPointService.deletePickupPoint(1L);

        // Then
        verify(pickupPointRepository).existsById(1L);
        verify(pickupPointRepository).deleteById(1L);
    }

    @Test
    void deletePickupPoint_ShouldThrowException_WhenPickupPointNotFound() {
        // Given
        when(pickupPointRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            pickupPointService.deletePickupPoint(1L);
        });

        verify(pickupPointRepository).existsById(1L);
        verify(pickupPointRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateLoad_ShouldReturnUpdatedPickupPointDto_WhenValidLoadChange() {
        // Given
        when(pickupPointRepository.findById(1L)).thenReturn(Optional.of(testPickupPoint));
        when(pickupPointRepository.save(any(PickupPoint.class))).thenReturn(testPickupPoint);
        when(pickupPointMapper.pickupPointToPickupPointDto(any(PickupPoint.class))).thenReturn(pickupPointDto);

        // When
        PickupPointDto result = pickupPointService.updateLoad(1L, 10);

        // Then
        assertNotNull(result);
        assertEquals(pickupPointDto, result);
        verify(pickupPointRepository).findById(1L);
        verify(pickupPointRepository).save(testPickupPoint);
        verify(pickupPointMapper).pickupPointToPickupPointDto(testPickupPoint);
    }

    @Test
    void updateLoad_ShouldThrowException_WhenInvalidLoadChange() {
        // Given
        when(pickupPointRepository.findById(1L)).thenReturn(Optional.of(testPickupPoint));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            pickupPointService.updateLoad(1L, 150); // Exceeds max capacity
        });

        verify(pickupPointRepository).findById(1L);
        verify(pickupPointRepository, never()).save(any());
    }

    @Test
    void updateLoad_ShouldThrowException_WhenPickupPointNotFound() {
        // Given
        when(pickupPointRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            pickupPointService.updateLoad(1L, 10);
        });

        verify(pickupPointRepository).findById(1L);
        verify(pickupPointRepository, never()).save(any());
    }
}
