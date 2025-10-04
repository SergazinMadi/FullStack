package org.example.fullstack.db.dto.request;

import org.example.fullstack.db.enums.PickupPointType;

import java.math.BigDecimal;

/**
 * Request DTO for updating PickupPoint
 */
public record PickupPointUpdateRequest(
    String name,
    String address,
    Long cityId,
    String phone,
    String workingHours,
    BigDecimal latitude,
    BigDecimal longitude,
    Integer maxCapacity,
    Double maxPackageWeight,
    Boolean hasColdStorage,
    Boolean isActive,
    PickupPointType type,
    Long managerId
) {}
