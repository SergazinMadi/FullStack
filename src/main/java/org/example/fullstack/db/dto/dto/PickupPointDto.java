package org.example.fullstack.db.dto.dto;

import org.example.fullstack.db.enums.PickupPointType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for PickupPoint
 */
public record PickupPointDto(
    Long id,
    String name,
    String address,
    Long cityId,
    String phone,
    String workingHours,
    BigDecimal latitude,
    BigDecimal longitude,
    Integer maxCapacity,
    Integer currentLoad,
    Double maxPackageWeight,
    Boolean hasColdStorage,
    Boolean isActive,
    PickupPointType type,
    Long managerId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
