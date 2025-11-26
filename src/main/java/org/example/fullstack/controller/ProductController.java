package org.example.fullstack.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstack.db.dto.dto.ProductDto;
import org.example.fullstack.db.dto.request.ProductCreateRequest;
import org.example.fullstack.db.dto.request.ProductUpdateRequest;
import org.example.fullstack.db.enums.ProductStatus;
import org.example.fullstack.db.model.User;
import org.example.fullstack.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@AuthenticationPrincipal User user, 
                                                   @RequestBody ProductCreateRequest request) {
        ProductDto product = productService.createProduct(user, request);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@AuthenticationPrincipal User user,
                                                   @PathVariable Long productId,
                                                   @RequestBody ProductUpdateRequest request) {
        ProductDto product = productService.updateProduct(user.getId(), request);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN', 'MANAGER', 'DRIVER')")
    public ResponseEntity<ProductDto> getProduct(@AuthenticationPrincipal User user,
                                                @PathVariable Long productId) {
        ProductDto product = productService.getProduct(user.getId(), productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'DRIVER')")
    public ResponseEntity<ProductDto> updateStatus(@AuthenticationPrincipal User user,
                                                  @PathVariable Long productId,
                                                  @RequestParam ProductStatus status) {
        ProductDto product = productService.updateStatus(user.getId(), productId, status);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public ResponseEntity<Void> deleteProduct(@AuthenticationPrincipal User user,
                                             @PathVariable Long productId) {
        productService.deleteProduct(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-products")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public ResponseEntity<List<ProductDto>> getMyProducts(@AuthenticationPrincipal User user) {
        // This would need to be implemented in ProductService
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<ProductDto>> getAllProducts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
