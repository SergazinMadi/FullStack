package org.example.fullstack.db.dto.dto;

import org.example.fullstack.db.enums.ProductCategory;
import org.example.fullstack.db.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Product}
 */
public record ProductDto(Long id, String name, String description, Double weight, Double length, Double width,
                         Double height, ProductCategory category, List<Long> statusHistoryIds, Boolean isFragile,
                         Boolean requiresColdStorage, Boolean isValuable, BigDecimal declaredValue, Long senderId,
                         Long receiverId, Long orderId, LocalDateTime createdAt, LocalDateTime updatedAt) {
}