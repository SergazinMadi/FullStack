package org.example.fullstack.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fullstack.db.dto.dto.PickupPointDto;
import org.example.fullstack.db.dto.request.PickupPointCreateRequest;
import org.example.fullstack.db.dto.request.PickupPointUpdateRequest;
import org.example.fullstack.db.enums.PickupPointType;
import org.example.fullstack.db.mapper.PickupPointMapper;
import org.example.fullstack.db.model.City;
import org.example.fullstack.db.model.PickupPoint;
import org.example.fullstack.db.repository.CityRepository;
import org.example.fullstack.db.repository.PickupPointRepository;
import org.example.fullstack.service.PickupPointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PickupPointServiceImpl implements PickupPointService {
    private final PickupPointRepository pickupPointRepository;
    private final PickupPointMapper pickupPointMapper;
    private final CityRepository cityRepository;

    @Override
    @Transactional
    public PickupPointDto createPickupPoint(PickupPointCreateRequest request) {
        log.info("Creating pickup point: {}", request.name());
        
        City city = cityRepository.findById(request.cityId())
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + request.cityId()));
        
        PickupPoint pickupPoint = pickupPointMapper.pickupPointCreateRequestToPickupPoint(request);
        pickupPoint.setCity(city);
        pickupPoint.setCurrentLoad(0);
        pickupPoint.setIsActive(true);
        
        PickupPoint savedPickupPoint = pickupPointRepository.save(pickupPoint);
        
        log.info("Pickup point created with ID: {}", savedPickupPoint.getId());
        return pickupPointMapper.pickupPointToPickupPointDto(savedPickupPoint);
    }

    @Override
    @Transactional
    public PickupPointDto updatePickupPoint(Long pickupPointId, PickupPointUpdateRequest request) {
        log.info("Updating pickup point with ID: {}", pickupPointId);
        
        PickupPoint pickupPoint = pickupPointRepository.findById(pickupPointId)
                .orElseThrow(() -> new RuntimeException("Pickup point not found with ID: " + pickupPointId));
        
        if (request.cityId() != null) {
            City city = cityRepository.findById(request.cityId())
                    .orElseThrow(() -> new RuntimeException("City not found with ID: " + request.cityId()));
            pickupPoint.setCity(city);
        }
        
        pickupPointMapper.updatePickupPointFromRequest(request, pickupPoint);
        PickupPoint updatedPickupPoint = pickupPointRepository.save(pickupPoint);
        
        log.info("Pickup point updated successfully");
        return pickupPointMapper.pickupPointToPickupPointDto(updatedPickupPoint);
    }

    @Override
    public PickupPointDto getPickupPoint(Long pickupPointId) {
        log.info("Getting pickup point with ID: {}", pickupPointId);
        
        PickupPoint pickupPoint = pickupPointRepository.findById(pickupPointId)
                .orElseThrow(() -> new RuntimeException("Pickup point not found with ID: " + pickupPointId));
        
        return pickupPointMapper.pickupPointToPickupPointDto(pickupPoint);
    }

    @Override
    public List<PickupPointDto> getAllPickupPoints() {
        log.info("Getting all pickup points");
        
        List<PickupPoint> pickupPoints = pickupPointRepository.findAll();
        return pickupPoints.stream()
                .map(pickupPointMapper::pickupPointToPickupPointDto)
                .toList();
    }

    @Override
    public List<PickupPointDto> getPickupPointsByCity(Long cityId) {
        log.info("Getting pickup points for city ID: {}", cityId);
        
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + cityId));
        
        List<PickupPoint> pickupPoints = pickupPointRepository.findByCityAndIsActive(city, true);
        return pickupPoints.stream()
                .map(pickupPointMapper::pickupPointToPickupPointDto)
                .toList();
    }

    @Override
    public List<PickupPointDto> getPickupPointsByType(PickupPointType type) {
        log.info("Getting pickup points by type: {}", type);
        
        List<PickupPoint> pickupPoints = pickupPointRepository.findByType(type);
        return pickupPoints.stream()
                .map(pickupPointMapper::pickupPointToPickupPointDto)
                .toList();
    }

    @Override
    public List<PickupPointDto> getAvailablePickupPoints(Long cityId) {
        log.info("Getting available pickup points for city ID: {}", cityId);
        
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + cityId));
        
        List<PickupPoint> pickupPoints = pickupPointRepository.findAvailablePickupPointsInCity(city);
        return pickupPoints.stream()
                .map(pickupPointMapper::pickupPointToPickupPointDto)
                .toList();
    }

    @Override
    public List<PickupPointDto> getAvailableColdStoragePickupPoints(Long cityId) {
        log.info("Getting available cold storage pickup points for city ID: {}", cityId);
        
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + cityId));
        
        List<PickupPoint> pickupPoints = pickupPointRepository.findAvailableColdStoragePickupPointsInCity(city);
        return pickupPoints.stream()
                .map(pickupPointMapper::pickupPointToPickupPointDto)
                .toList();
    }

    @Override
    @Transactional
    public void deletePickupPoint(Long pickupPointId) {
        log.info("Deleting pickup point with ID: {}", pickupPointId);
        
        if (!pickupPointRepository.existsById(pickupPointId)) {
            throw new RuntimeException("Pickup point not found with ID: " + pickupPointId);
        }
        
        pickupPointRepository.deleteById(pickupPointId);
        log.info("Pickup point deleted successfully");
    }

    @Override
    @Transactional
    public PickupPointDto updateLoad(Long pickupPointId, Integer loadChange) {
        log.info("Updating load for pickup point ID: {} by {}", pickupPointId, loadChange);
        
        PickupPoint pickupPoint = pickupPointRepository.findById(pickupPointId)
                .orElseThrow(() -> new RuntimeException("Pickup point not found with ID: " + pickupPointId));
        
        int newLoad = pickupPoint.getCurrentLoad() + loadChange;
        if (newLoad < 0 || newLoad > pickupPoint.getMaxCapacity()) {
            throw new RuntimeException("Invalid load change. Current load: " + pickupPoint.getCurrentLoad() + 
                    ", change: " + loadChange + ", max capacity: " + pickupPoint.getMaxCapacity());
        }
        
        pickupPoint.setCurrentLoad(newLoad);
        PickupPoint updatedPickupPoint = pickupPointRepository.save(pickupPoint);
        
        log.info("Load updated successfully. New load: {}", newLoad);
        return pickupPointMapper.pickupPointToPickupPointDto(updatedPickupPoint);
    }
}
