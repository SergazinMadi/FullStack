package org.example.fullstack.db.dto.dto;

import org.example.fullstack.db.enums.UserRole;

import java.time.LocalDateTime;

/**
 * DTO for User
 */
public record UserDto(
    Long id,
    String username,
    String email,
    String firstName,
    String lastName,
    String phone,
    UserRole role,
    Boolean isActive,
    String licenseNumber,
    String vehicleType,
    String vehicleNumber,
    Double maxLoadCapacity,
    String address,
    Long cityId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
