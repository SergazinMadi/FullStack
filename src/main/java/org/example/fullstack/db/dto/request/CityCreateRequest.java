package org.example.fullstack.db.dto.request;

/**
 * Request DTO for creating City
 */
public record CityCreateRequest(
    String name,
    String country
) {}
