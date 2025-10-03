package org.example.fullstack.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.fullstack.db.dto.dto.ProductDto;
import org.example.fullstack.db.dto.request.ProductCreateRequest;
import org.example.fullstack.db.dto.request.ProductUpdateRequest;
import org.example.fullstack.db.enums.ProductStatus;
import org.example.fullstack.db.enums.UserRole;
import org.example.fullstack.db.mapper.ProductMapper;
import org.example.fullstack.db.mapper.UserMapper;
import org.example.fullstack.db.model.Product;
import org.example.fullstack.db.model.ProductStatusHistory;
import org.example.fullstack.db.model.User;
import org.example.fullstack.db.repository.ProductRepository;
import org.example.fullstack.db.repository.ProductStatusHistoryRepository;
import org.example.fullstack.db.repository.UserRepository;
import org.example.fullstack.service.ProductService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final ProductStatusHistoryRepository productStatusHistoryRepository;
    
    @Override
    public ProductDto createProduct(Long user_id, ProductCreateRequest request) {
        User user = userRepository.getUserById(user_id).orElseThrow();
        Product product = productMapper.productCreateRequestToProduct(request);
        if (user.getRole() == UserRole.MANAGER) {
            productStatusHistoryRepository.create()
        }
        product.set
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

    }
}
