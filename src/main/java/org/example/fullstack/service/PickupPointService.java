package org.example.fullstack.service;

import org.example.fullstack.db.dto.dto.PickupPointDto;
import org.example.fullstack.db.dto.request.PickupPointCreateRequest;
import org.example.fullstack.db.dto.request.PickupPointUpdateRequest;
import org.example.fullstack.db.enums.PickupPointType;

import java.util.List;

public interface PickupPointService {
    PickupPointDto createPickupPoint(PickupPointCreateRequest request);
    PickupPointDto updatePickupPoint(Long pickupPointId, PickupPointUpdateRequest request);
    PickupPointDto getPickupPoint(Long pickupPointId);
    List<PickupPointDto> getAllPickupPoints();
    List<PickupPointDto> getPickupPointsByCity(Long cityId);
    List<PickupPointDto> getPickupPointsByType(PickupPointType type);
    List<PickupPointDto> getAvailablePickupPoints(Long cityId);
    List<PickupPointDto> getAvailableColdStoragePickupPoints(Long cityId);
    void deletePickupPoint(Long pickupPointId);
    PickupPointDto updateLoad(Long pickupPointId, Integer loadChange);
}
