package org.example.fullstack.service;

import org.example.fullstack.db.dto.dto.ProductDto;
import org.example.fullstack.db.dto.request.ProductCreateRequest;
import org.example.fullstack.db.dto.request.ProductUpdateRequest;
import org.example.fullstack.db.enums.ProductStatus;

public interface ProductService {
    ProductDto createProduct(Long user_id, ProductCreateRequest request);
    ProductDto updateProduct(Long user_id, ProductUpdateRequest request);
    ProductDto getProduct(Long user_id, Long product_id);
    ProductDto updateStatus(Long user_id, Long product_id, ProductStatus status);
    void deleteProduct(Long user_id, Long product_id);
}
