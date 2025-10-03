package org.example.fullstack.db.model;

import jakarta.persistence.*;
import org.example.fullstack.db.enums.PickupPointType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pickup_points")
public class PickupPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @ManyToOne()
    @JoinColumn(name = "city_id")
    private City city;

    @Column(length = 20)
    private String phone;
    
    @Column(name = "working_hours", length = 100)
    private String workingHours; // например: "Пн-Пт: 9:00-18:00, Сб-Вс: 10:00-16:00"
    
    // Координаты для навигации
    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;
    
    // Характеристики пункта выдачи
    @Column(name = "max_capacity")
    private Integer maxCapacity; // Максимальное количество посылок
    
    @Column(name = "current_load")
    private Integer currentLoad = 0; // Текущая загруженность
    
    @Column(name = "max_package_weight")
    private Double maxPackageWeight; // Макс. вес одной посылки (кг)
    
    @Column(name = "has_cold_storage")
    private Boolean hasColdStorage = false; // Есть ли холодильник

    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Enumerated(EnumType.STRING)
    private PickupPointType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager; // Менеджер пункта выдачи

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Вычисляемые поля
    public Double getLoadPercentage() {
        if (maxCapacity == 0) return 0.0;
        return (currentLoad.doubleValue() / maxCapacity.doubleValue()) * 100;
    }
    
    public boolean canAcceptPackage(Double weight, boolean needsColdStorage) {
        if (!isActive) return false;
        if (currentLoad >= maxCapacity) return false;
        if (weight > maxPackageWeight) return false;
        if (needsColdStorage && !hasColdStorage) return false;
        return true;
    }
    
    public Integer getAvailableCapacity() {
        return maxCapacity - currentLoad;
    }
}

