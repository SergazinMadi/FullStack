package org.example.fullstack.service;

import org.example.fullstack.db.dto.dto.ProductDto;
import org.example.fullstack.db.dto.request.ProductCreateRequest;
import org.example.fullstack.db.dto.request.ProductUpdateRequest;
import org.example.fullstack.db.enums.ProductCategory;
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
import org.example.fullstack.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductStatusHistoryRepository productStatusHistoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private User testUser;
    private User testReceiver;
    private Product testProduct;
    private ProductCreateRequest productCreateRequest;
    private ProductUpdateRequest productUpdateRequest;
    private ProductDto productDto;
    private ProductStatusHistory productStatusHistory;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("sender");
        testUser.setRole(UserRole.CLIENT);

        testReceiver = new User();
        testReceiver.setId(2L);
        testReceiver.setUsername("receiver");
        testReceiver.setRole(UserRole.CLIENT);

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setWeight(1.5);
        testProduct.setLength(10.0);
        testProduct.setWidth(5.0);
        testProduct.setHeight(3.0);
        testProduct.setCategory(ProductCategory.ELECTRONICS);
        testProduct.setIsFragile(false);
        testProduct.setRequiresColdStorage(false);
        testProduct.setIsValuable(false);
        testProduct.setDeclaredValue(new BigDecimal("100.00"));
        testProduct.setSender(testUser);
        testProduct.setReceiver(testReceiver);

        productCreateRequest = new ProductCreateRequest(
                "Test Product",
                "Test Description",
                1.5, // weight
                10.0, // length
                5.0, // width
                3.0, // height
                ProductCategory.ELECTRONICS,
                false, // isFragile
                false, // requiresColdStorage
                false, // isValuable
                2L // receiverId
        );

        productUpdateRequest = new ProductUpdateRequest(
                "Updated Product",
                "Updated Description",
                2.0, // weight
                12.0, // length
                6.0, // width
                4.0, // height
                ProductCategory.CLOTHING,
                true, // isFragile
                false, // requiresColdStorage
                true, // isValuable
                new BigDecimal("200.00"), // declaredValue
                2L, // receiverId
                1L // orderId
        );

        productDto = new ProductDto(
                1L,
                "Test Product",
                "Test Description",
                1.5, // weight
                10.0, // length
                5.0, // width
                3.0, // height
                ProductCategory.ELECTRONICS,
                List.of(1L), // statusHistoryIds
                false, // isFragile
                false, // requiresColdStorage
                false, // isValuable
                new BigDecimal("100.00"), // declaredValue
                1L, // senderId
                2L, // receiverId
                null, // orderId
                null, // createdAt
                null // updatedAt
        );

        productStatusHistory = new ProductStatusHistory();
        productStatusHistory.setId(1L);
        productStatusHistory.setProduct(testProduct);
        productStatusHistory.setStatus(ProductStatus.CREATED);
    }

    @Test
    void createProduct_ShouldReturnProductDto_WhenValidRequest() {
        // Given
        when(productMapper.productCreateRequestToProduct(any(ProductCreateRequest.class)))
                .thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);
        when(productStatusHistoryRepository.save(any(ProductStatusHistory.class)))
                .thenReturn(productStatusHistory);

        // When
        ProductDto result = productService.createProduct(testUser, productCreateRequest);

        // Then
        assertNotNull(result);
        assertEquals(productDto, result);
        verify(productMapper).productCreateRequestToProduct(productCreateRequest);
        verify(productRepository).save(testProduct);
        verify(productMapper).productToProductDto(testProduct);
        verify(productStatusHistoryRepository).save(any(ProductStatusHistory.class));
    }

    @Test
    void createProduct_ShouldSetSender_WhenCreatingProduct() {
        // Given
        when(productMapper.productCreateRequestToProduct(any(ProductCreateRequest.class)))
                .thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);
        when(productStatusHistoryRepository.save(any(ProductStatusHistory.class)))
                .thenReturn(productStatusHistory);

        // When
        productService.createProduct(testUser, productCreateRequest);

        // Then
        verify(productMapper).productCreateRequestToProduct(productCreateRequest);
        verify(productRepository).save(testProduct);
        assertEquals(testUser, testProduct.getSender());
    }

    @Test
    void createProduct_ShouldCreateStatusHistory_WhenCreatingProduct() {
        // Given
        when(productMapper.productCreateRequestToProduct(any(ProductCreateRequest.class)))
                .thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);
        when(productStatusHistoryRepository.save(any(ProductStatusHistory.class)))
                .thenReturn(productStatusHistory);

        // When
        productService.createProduct(testUser, productCreateRequest);

        // Then
        verify(productStatusHistoryRepository).save(any(ProductStatusHistory.class));
    }

    @Test
    void updateProduct_ShouldReturnNull_WhenNotImplemented() {
        // Given
        // When
        ProductDto result = productService.updateProduct(1L, productUpdateRequest);

        // Then
        assertNull(result);
    }

    @Test
    void getProduct_ShouldReturnNull_WhenNotImplemented() {
        // Given
        // When
        ProductDto result = productService.getProduct(1L, 1L);

        // Then
        assertNull(result);
    }

    @Test
    void updateStatus_ShouldReturnNull_WhenNotImplemented() {
        // Given
        // When
        ProductDto result = productService.updateStatus(1L, 1L, ProductStatus.READY_FOR_PICKUP);

        // Then
        assertNull(result);
    }

    @Test
    void deleteProduct_ShouldDoNothing_WhenNotImplemented() {
        // Given
        // When
        productService.deleteProduct(1L, 1L);

        // Then
        // No exception should be thrown
        assertTrue(true);
    }
}
