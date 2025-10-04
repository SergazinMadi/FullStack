package org.example.fullstack.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstack.db.dto.dto.PickupPointDto;
import org.example.fullstack.db.dto.request.PickupPointCreateRequest;
import org.example.fullstack.db.dto.request.PickupPointUpdateRequest;
import org.example.fullstack.db.enums.PickupPointType;
import org.example.fullstack.service.PickupPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pickup-points")
@RequiredArgsConstructor
public class PickupPointController {
    private final PickupPointService pickupPointService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<PickupPointDto> createPickupPoint(@RequestBody PickupPointCreateRequest request) {
        PickupPointDto pickupPoint = pickupPointService.createPickupPoint(request);
        return ResponseEntity.ok(pickupPoint);
    }

    @PutMapping("/{pickupPointId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<PickupPointDto> updatePickupPoint(@PathVariable Long pickupPointId, 
                                                           @RequestBody PickupPointUpdateRequest request) {
        PickupPointDto pickupPoint = pickupPointService.updatePickupPoint(pickupPointId, request);
        return ResponseEntity.ok(pickupPoint);
    }

    @GetMapping("/{pickupPointId}")
    public ResponseEntity<PickupPointDto> getPickupPoint(@PathVariable Long pickupPointId) {
        PickupPointDto pickupPoint = pickupPointService.getPickupPoint(pickupPointId);
        return ResponseEntity.ok(pickupPoint);
    }

    @GetMapping
    public ResponseEntity<List<PickupPointDto>> getAllPickupPoints() {
        List<PickupPointDto> pickupPoints = pickupPointService.getAllPickupPoints();
        return ResponseEntity.ok(pickupPoints);
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<PickupPointDto>> getPickupPointsByCity(@PathVariable Long cityId) {
        List<PickupPointDto> pickupPoints = pickupPointService.getPickupPointsByCity(cityId);
        return ResponseEntity.ok(pickupPoints);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PickupPointDto>> getPickupPointsByType(@PathVariable PickupPointType type) {
        List<PickupPointDto> pickupPoints = pickupPointService.getPickupPointsByType(type);
        return ResponseEntity.ok(pickupPoints);
    }

    @GetMapping("/available/city/{cityId}")
    public ResponseEntity<List<PickupPointDto>> getAvailablePickupPoints(@PathVariable Long cityId) {
        List<PickupPointDto> pickupPoints = pickupPointService.getAvailablePickupPoints(cityId);
        return ResponseEntity.ok(pickupPoints);
    }

    @GetMapping("/available/cold-storage/city/{cityId}")
    public ResponseEntity<List<PickupPointDto>> getAvailableColdStoragePickupPoints(@PathVariable Long cityId) {
        List<PickupPointDto> pickupPoints = pickupPointService.getAvailableColdStoragePickupPoints(cityId);
        return ResponseEntity.ok(pickupPoints);
    }

    @PutMapping("/{pickupPointId}/load")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<PickupPointDto> updateLoad(@PathVariable Long pickupPointId, 
                                                    @RequestParam Integer loadChange) {
        PickupPointDto pickupPoint = pickupPointService.updateLoad(pickupPointId, loadChange);
        return ResponseEntity.ok(pickupPoint);
    }

    @DeleteMapping("/{pickupPointId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deletePickupPoint(@PathVariable Long pickupPointId) {
        pickupPointService.deletePickupPoint(pickupPointId);
        return ResponseEntity.noContent().build();
    }
}
