package org.example.fullstack.db.dto.request;

import org.example.fullstack.db.enums.PickupPointType;

import java.math.BigDecimal;

/**
 * Request DTO for creating PickupPoint
 */
public record PickupPointCreateRequest(
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
    PickupPointType type,
    Long managerId
) {}
