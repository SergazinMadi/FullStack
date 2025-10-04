package org.example.fullstack.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.fullstack.db.enums.ProductCategory;
import org.example.fullstack.db.enums.ProductStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Double weight; // вес в кг
    
    @Column(nullable = false)
    private Double length; // длина в см
    
    @Column(nullable = false)
    private Double width; // ширина в см
    
    @Column(nullable = false)
    private Double height; // высота в см
    
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(name = "is_fragile")
    private Boolean isFragile = false;
    
    @Column(name = "requires_cold_storage")
    private Boolean requiresColdStorage = false;
    
    @Column(name = "is_valuable")
    private Boolean isValuable = false; // Ценный груз
    
    @Column(name = "declared_value", precision = 10, scale = 2)
    private BigDecimal declaredValue; // Объявленная стоимость

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // Кто отправляет

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

