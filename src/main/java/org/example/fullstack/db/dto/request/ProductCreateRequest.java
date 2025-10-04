package org.example.fullstack.db.dto.request;

import org.example.fullstack.db.enums.ProductCategory;
import org.example.fullstack.db.model.Product;

/**
 * DTO for {@link Product}
 */
public record ProductCreateRequest(String name,
                                   String description,
                                   Double weight,
                                   Double length,
                                   Double width,
                                   Double height,
                                   ProductCategory category,
                                   Boolean isFragile,
                                   Boolean requiresColdStorage,
                                   Boolean isValuable,
                                   Long receiverId) {
}