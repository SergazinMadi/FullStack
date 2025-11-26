package org.example.fullstack.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fullstack.db.dto.dto.ProductDto;
import org.example.fullstack.db.dto.request.ProductCreateRequest;
import org.example.fullstack.db.dto.request.ProductUpdateRequest;
import org.example.fullstack.db.enums.ProductStatus;
import org.example.fullstack.db.mapper.ProductMapper;
import org.example.fullstack.db.model.Product;
import org.example.fullstack.db.model.ProductStatusHistory;
import org.example.fullstack.db.model.User;
import org.example.fullstack.db.repository.ProductRepository;
import org.example.fullstack.db.repository.ProductStatusHistoryRepository;
import org.example.fullstack.db.repository.UserRepository;
import org.example.fullstack.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final ProductStatusHistoryRepository productStatusHistoryRepository;

    @Override
    public ProductDto createProduct(User user, ProductCreateRequest request) {
        log.debug("Creating product for user {}", user);
        log.info("Creating product for user {}", request);
        Product product = productMapper.productCreateRequestToProduct(request);
        product.setSender(user);
        ProductDto productDto = productMapper.productToProductDto(productRepository.save(product));

        ProductStatusHistory productStatusHistory = new ProductStatusHistory();
        productStatusHistory.setProduct(product);
        productStatusHistory.setStatus(ProductStatus.CREATED);
        productStatusHistory.setChangedAt(LocalDateTime.now());
        productStatusHistoryRepository.save(productStatusHistory);

        return productDto;
    }

    @Override
    public ProductDto updateProduct(Long user_id, ProductUpdateRequest request) {
        return null;
    }

    @Override
    public ProductDto getProduct(Long user_id, Long product_id) {
        return null;
    }

    @Override
    public ProductDto updateStatus(Long user_id, Long product_id, ProductStatus status) {
        return null;
    }

    @Override
    public void deleteProduct(Long user_id, Long product_id) {
        productRepository.deleteById(product_id);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        products.forEach(product -> log.info("Found product {}", product));

        return products.stream().map(productMapper::productToProductDto).toList();
    }

}
